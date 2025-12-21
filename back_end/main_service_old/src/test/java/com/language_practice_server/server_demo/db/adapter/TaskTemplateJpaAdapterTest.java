package com.language_practice_server.server_demo.db.adapter;

import com.language_practice_server.server_demo.config.TestAuditorConfig;
import com.language_practice_server.server_demo.db.entity.TaskTemplateEntity;
import com.language_practice_server.server_demo.db.repository.TaskTemplateRepositoryJpa;
import com.language_practice_server.server_demo.common.enums.TaskType;
import com.language_practice_server.server_demo.domain.model.TaskTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.language_practice_server.server_demo.domain.repository.TaskTemplateRepository;
import com.language_practice_server.server_demo.mapper.TaskTemplateMapperImpl;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("adapter")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import({TaskTemplateJpaAdapter.class, TaskTemplateMapperImpl.class, TestAuditorConfig.class})
public class TaskTemplateJpaAdapterTest {
    @Autowired
    private TaskTemplateRepositoryJpa repositoryJpa;

    @Autowired
    private TaskTemplateRepository templateRepository;

    @BeforeEach
    void setup() {
        repositoryJpa.deleteAll();
        Long date = new Date().getTime();
        TaskTemplateEntity template1 = new TaskTemplateEntity(null, "Test 1", TaskType.TEST, 1L);
        TaskTemplateEntity template2 = new TaskTemplateEntity(null, "Test 2", TaskType.TEST, 1L);
        TaskTemplateEntity template3 = new TaskTemplateEntity(null, "Test 3", TaskType.TEST, 2L);
        repositoryJpa.saveAll(List.of(template1, template2, template3));
        repositoryJpa.flush();
    }

    @Test
    public void saveReturnsTemplate() {
        Long date = new Date().getTime();
        TaskTemplate template = new TaskTemplate(null, "Test 4", "test 4", TaskType.TEST, 2L);
        TaskTemplate result = templateRepository.save(template);

        assertNotNull(result.getId());
        assertFalse(result.isDeleted());
        assertNotEquals(result.getClass(), TaskTemplateEntity.class);
        assertTrue(result.getTitle().contains("Test 4"));
        assertTrue(result.getDescription().contains("test 4"));
    }

    @Test
    public void updateReturnsUpdatedObject() {
        Long date = new Date().getTime();
        TaskTemplateEntity existing = repositoryJpa.findAll().get(0);
        Long existingId = existing.getId();
        TaskTemplate templateToUpdate = new TaskTemplate(existingId, "Test 5", "test 5", TaskType.TEST, 2L);
        TaskTemplate result = templateRepository.update(templateToUpdate);

        assertEquals(existingId, result.getId());
        assertEquals(templateToUpdate.getId(), result.getId());
        assertThat(result.getTitle()).isEqualTo(templateToUpdate.getTitle());
        assertThat(result.getDescription()).isEqualTo(templateToUpdate.getDescription());
    }

    @Test
    public void findByIdReturnsObjectWhenExists() {
        TaskTemplateEntity entity = repositoryJpa.findAll().get(0);
        Optional<TaskTemplate> result = templateRepository.findTaskTemplateById(entity.getId());

        assertThat(result).isPresent();

        TaskTemplate template = result.get();

        assertThat(template.getId()).isEqualTo(entity.getId());
        assertThat(template.getTitle()).isEqualTo(entity.getTitle());
        assertThat(template.getDescription()).isEqualTo(entity.getDescription());
        assertFalse(template.isDeleted());
    }

    @Test
    public void deleteMarksObjectAsDeleted() {
        TaskTemplateEntity existing = repositoryJpa.findAll().get(0);
        templateRepository.delete(existing.getId());
        Optional<TaskTemplate> deletedTemplate = templateRepository.findTaskTemplateById(existing.getId());

        assertThat(deletedTemplate).isPresent();

        TaskTemplate result = deletedTemplate.get();

        assertThat(result.getId()).isEqualTo(existing.getId());
        assertTrue(result.isDeleted());
    }

    @Test
    public void findAllByCreatorIdReturnsPage() {
        Page<TaskTemplate> result = templateRepository.findAllTaskTemplateByCreatorId(1L, PageRequest.of(0, 10));

        AssertionsForInterfaceTypes.assertThat(result.getContent()).isNotEmpty();
        AssertionsForInterfaceTypes.assertThat(result.getContent()).allMatch(r -> r.getOwnerId().equals(1L));
        assertThat(result.getContent().size()).isEqualTo(2);
    }
}
