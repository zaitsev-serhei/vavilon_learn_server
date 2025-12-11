package com.language_practice_server.server_demo.db.adapter;

import com.language_practice_server.server_demo.config.TestAuditorConfig;
import com.language_practice_server.server_demo.db.entity.TaskEntity;
import com.language_practice_server.server_demo.db.repository.TaskRepositoryJpa;
import com.language_practice_server.server_demo.domain.model.Task;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.language_practice_server.server_demo.domain.repository.TaskRepository;
import com.language_practice_server.server_demo.mapper.TaskMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("adapter")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import({TaskJpaAdapter.class, TaskMapperImpl.class, TestAuditorConfig.class})
public class TaskJpaAdapterTest {
    @Autowired
    private TaskRepositoryJpa repositoryJpa;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setup() {
        repositoryJpa.deleteAll();
        TaskEntity task1 = new TaskEntity(null, 1L, 100L, "instr");
        TaskEntity task2 = new TaskEntity(null, 4L, 300L, "instr");
        TaskEntity task3 = new TaskEntity(null, 4L, 300L, "instr");
        repositoryJpa.saveAll(List.of(task1, task2, task3));
        repositoryJpa.flush();
        /*
        реальності після збереження в H2 ідентифікатори можуть мати інші значення (не гарантовано
        починаються з 1 в кожному запуску або після deleteAll() — sequence не скидається).
         */
    }

    @Test
    public void saveReturnsTask() {
        Long date = new Date().getTime();
        Task task4 = new Task(null, 4L, 300L, "instr");
        Task result = taskRepository.saveTask(task4);
        assertNotNull(result.getId());
        assertFalse(result.isDeleted());
        assertNotEquals(result.getClass(), TaskEntity.class);
        assertTrue(result.getInstructions().contains("instr"));
    }

    @Test
    public void updateReturnsUpdatedObject() {
        Long date = new Date().getTime();
        TaskEntity existing = repositoryJpa.findAll().get(0);
        Long existingId = existing.getId();
        Task taskToUpdate = new Task(existingId, 1L, 100L, "updated");
        Task result = taskRepository.updateTask(taskToUpdate);

        assertEquals(existingId, result.getId());
        assertEquals(taskToUpdate.getId(), result.getId());
        assertEquals(taskToUpdate.getInstructions(), result.getInstructions());
        assertTrue(result.getInstructions().contains("updated"));
    }

    @Test
    public void findByIdReturnsObjectWhenExists() {
        TaskEntity entity = repositoryJpa.findAll().get(0);
        Optional<Task> result = taskRepository.findById(entity.getId());

        assertThat(result).isPresent();
        Task task = result.get();
        assertThat(task.getId()).isEqualTo(entity.getId());
        assertThat(task.getInstructions()).isEqualTo(entity.getInstructions());
        assertThat(task.getTaskTemplateId()).isEqualTo(entity.getTaskTemplateId());
    }

    @Test
    public void deleteMarksObjectAsDeleted() {
        TaskEntity existing = repositoryJpa.findAll().get(0);
        taskRepository.delete(existing.getId());
        Optional<Task> deletedTask = taskRepository.findById(existing.getId());

        assertThat(deletedTask).isPresent();

        Task result = deletedTask.get();

        assertThat(result.getId()).isEqualTo(existing.getId());
        assertTrue(result.isDeleted());
    }

    @Test
    public void findAllByCreatorIdReturnsPage() {
        Page<Task> result = taskRepository.findAllTaskByCreatorId(300L, PageRequest.of(0, 10));

        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent()).allMatch(r -> r.getOwnerId().equals(300L));
        assertThat(result.getContent().size()).isEqualTo(2);
    }
}
