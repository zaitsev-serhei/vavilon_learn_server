package com.language_practice_server.server_demo.service;

import com.language_practice_server.server_demo.domain.model.Task;
import com.language_practice_server.server_demo.domain.repository.TaskRepository;
import java.util.List;
import java.util.Optional;
import com.language_practice_server.server_demo.kafka.KafkaEventProducer;
import com.language_practice_server.server_demo.service.impl.TaskServiceImpl;
import com.language_practice_server.server_demo.web.security.config.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;


@Tag("service")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@Transactional
public class TaskServiceIT {
    @Autowired
    private TaskRepository repository;
    @MockitoBean
    KafkaEventProducer producer;

    private TaskService service;

    @BeforeEach
    public void setUp(){
        producer = mock(KafkaEventProducer.class);
        service = new TaskServiceImpl(repository,producer);
        //setting up mock Authentication to allow SecurityAudit configuration for proper DB access
        JwtAuthenticationFilter.AuthenticatedUser principal = new JwtAuthenticationFilter.AuthenticatedUser(111L, "test user");
        var auth = new UsernamePasswordAuthenticationToken(principal, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void createTaskThrowsErrorWhenOwnerIdMissing() {
        Task task = new Task();
        task.setTaskTemplateId(111L);
        task.setInstructions("test");

        assertThrows(IllegalArgumentException.class, () -> service.createTask(task));
    }

    @Test
    public void createTaskThrowsErrorWhenTemplateIdMissing() {
        Task task = new Task();
        task.setOwnerId(112L);
        task.setInstructions("test");

        assertThrows(IllegalArgumentException.class, () -> service.createTask(task));
    }

    @Test
    public void createReturnsTaskWithId() {
        Task task = new Task();
        task.setOwnerId(1L);
        task.setTaskTemplateId(113L);
        task.setInstructions("test");

        Task saved = service.createTask(task);
        assertThat(saved.getId()).isNotNull();

    }

    @Test
    public void updateReturnsTaskWithUpdatedFields() {
        Task task = new Task(null, 114L, 1L, "test1");

        Task saved = service.createTask(task);
        saved.setOwnerId(2L);
        saved.setInstructions("test 2");

        Task updated = service.updateTask(saved);

        assertThat(updated.getOwnerId()).isEqualTo(saved.getOwnerId());
        assertThat(updated.getInstructions()).contains("test 2");
    }

    @Test
    public void findByIdReturnSavedTask() {
        Task task = new Task(null, 115L, 1L, "test1");

        Task saved = service.createTask(task);
        Task result = service.findTaskById(saved.getId());

        assertThat(result.getClass()).isNotNull();
        assertThat(result.getId()).isEqualTo(saved.getId());
        assertThat(result.getOwnerId()).isEqualTo(saved.getOwnerId());
        assertThat(result.getInstructions()).contains("test1");

        assertThat(result.getLastModifiedBy()).isNotNull();
        assertThat(result.getCreatedBy()).isNotNull();
    }

    @Test
    public void deleteMarksTaskAsDeleted() {
        Task task = new Task(null, 116L, 1L, "test1");

        Task saved = service.createTask(task);
        service.delete(saved.getId());

        Optional<Task> result = repository.findById(saved.getId());

        assertThat(result).isPresent();
        assertTrue(result.get().isDeleted());
    }

    @Test
    public void findByOwnerIdReturnsPageWithTasks() {
        Task task1 = new Task(null, 117L, 1L, "test1");
        Task task2 = new Task(null, 2L, 1L, "test1");
        Task task3 = new Task(null, 3L, 2L, "test1");
        service.createTask(task1);
        service.createTask(task2);
        service.createTask(task3);

        Page<Task> result = service.findAllTasksByOwnerId(1L, PageRequest.of(0, 10));

        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent()).allMatch(r -> r.getOwnerId().equals(1L));
        assertThat(result.getContent().size()).isEqualTo(2);
    }
}
