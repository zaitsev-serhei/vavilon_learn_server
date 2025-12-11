package com.language_practice_server.server_demo.service;

import com.language_practice_server.server_demo.common.EventTopics;
import com.language_practice_server.server_demo.domain.model.Task;
import com.language_practice_server.server_demo.domain.repository.TaskRepository;
import com.language_practice_server.server_demo.kafka.KafkaEventProducer;
import com.language_practice_server.server_demo.service.impl.TaskServiceImpl;
import com.language_practice_server.server_demo.web.security.config.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("service")
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository repository;
    @Mock
    private KafkaEventProducer producer;
    @InjectMocks
    private TaskServiceImpl taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = createTask(null, 10L, 100L);
        JwtAuthenticationFilter.AuthenticatedUser principal = new JwtAuthenticationFilter.AuthenticatedUser(111L, "test user");
        var auth = new UsernamePasswordAuthenticationToken(principal, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void createTaskSuccessProducesEvent() {
        Task toSave = createTask(1L, sampleTask.getTaskTemplateId(), sampleTask.getOwnerId());
        when(repository.saveTask(sampleTask)).thenReturn(toSave);
        //we do not want to publish actual events on tests. Using mocks instead
        doNothing().when(producer).publish(anyString(), anyString(), any());

        Task result = taskService.createTask(sampleTask);

        assertNotNull(result);
        assertEquals(result.getId(), toSave.getId());
        verify(repository, times(1)).saveTask(sampleTask);

        //we want to verify if Kafka producer is called with arguments
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(producer, times(1)).publish(eq(EventTopics.TASK_CREATED), eq(sampleTask.getOwnerId().toString()), eventCaptor.capture());
        assertNotNull(eventCaptor.getValue());
    }

    @Test
    public void updateTaskCallsReposOnes() {
        Task toUpdate = createTask(1L, 101L, 111L);
        when(repository.updateTask(sampleTask)).thenReturn(toUpdate);

        Task updated = taskService.updateTask(sampleTask);

        assertNotNull(updated);
        verify(repository, times(1)).updateTask(sampleTask);
        assertEquals(updated.getId(), toUpdate.getId());
        assertEquals(updated.getTaskTemplateId(), toUpdate.getTaskTemplateId());
        assertEquals(updated.getOwnerId(), toUpdate.getOwnerId());
    }

    @Test
    public void findTaskCallsRepoOnesAndReturnsTask() {
        Task toFind = createTask(1L, 101L, 111L);
        when(repository.findById(1L)).thenReturn(Optional.of(toFind));

        Task result = taskService.findTaskById(1L);

        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
        assertEquals(toFind.getId(), result.getId());
    }

    @Test
    public void findByOwnerIdCallsRepoOnesAndReturnPageWithTasks() {
        Pageable pageable = PageRequest.of(1, 5);
        Task task1 = createTask(1L, 101L, 111L);
        Task task2 = createTask(2L, 10L, 111L);
        Task task3 = createTask(3L, 121L, 111L);
        Task task4 = createTask(4L, 111L, 111L);
        Page<Task> pageToFind = new PageImpl<>(List.of(task1
                , task2
                , task3
                , task4));
        when(repository.findAllTaskByCreatorId(111L, pageable)).thenReturn(pageToFind);

        Page<Task> result = taskService.findAllTasksByOwnerId(111L, pageable);

        verify(repository, times(1)).findAllTaskByCreatorId(111L, pageable);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.getTotalElements()).isEqualTo(4);
        assertThat(result.getContent()).containsExactlyInAnyOrder(task1,task2,task3,task4);
    }


    private Task createTask(Long id, Long templateId, Long ownerId) {
        Task task = new Task();
        task.setId(id);
        task.setTaskTemplateId(templateId);
        task.setOwnerId(ownerId);
        task.setInstructions("Test");
        return task;
    }
}
