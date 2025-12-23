package com.language_practice_server.server_demo.db.repository;

import com.language_practice_server.server_demo.config.TestAuditorConfig;
import com.language_practice_server.server_demo.db.entity.TaskTemplateEntity;
import com.language_practice_server.server_demo.common.enums.TaskType;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("jpa_repository")
@DataJpaTest
@Import(TestAuditorConfig.class)
public class TaskTemplateRepositoryJpaTest {
    @Autowired
    private TaskTemplateRepositoryJpa repository;

    @Test
    public void findByCreatorIdReturnPage() {
        TaskTemplateEntity template1 = new TaskTemplateEntity(null, "Test 1", TaskType.TEST, 1L);
        TaskTemplateEntity template2 = new TaskTemplateEntity(null, "Test 2", TaskType.TEST, 1L);
        TaskTemplateEntity template3 = new TaskTemplateEntity(null, "Test 3", TaskType.TEST, 2L);

        repository.save(template1);
        repository.save(template2);
        repository.save(template3);

        Page<TaskTemplateEntity> result = repository.findByOwnerId(1L, PageRequest.of(0, 10));

        assertEquals(result.getTotalElements(), 2L);
        assertThat(result.getContent()).extracting(TaskTemplateEntity::getOwnerId).containsOnly(1L);
        assertThat(result.getContent()).extracting(TaskTemplateEntity::isDeleted).containsOnly(false);

        assertThat(result.getContent()).extracting(TaskTemplateEntity::getCreatedBy).containsOnly(111L);
        assertThat(result.getContent()).extracting(TaskTemplateEntity::getLastModifiedBy).containsOnly(111L);
        assertNotNull(result.getContent().get(0).getCreatedAt());
    }
}
