package com.language_practice_server.server_demo.db.repository;

import com.language_practice_server.server_demo.config.TestAuditorConfig;
import com.language_practice_server.server_demo.db.entity.TaskEntity;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("jpa_repository")
@DataJpaTest
@Import(TestAuditorConfig.class)
public class TaskRepositoryJpaTest {
    @Autowired
    private TaskRepositoryJpa repository;

    @Test
    public void findByCreatorIdReturnPage() {
        TaskEntity task1 = new TaskEntity(null, 1L, 100L, "instr");
        TaskEntity task2 = new TaskEntity(null, 1L, 100L, "instr");
        TaskEntity task3 = new TaskEntity(null, 4L, 300L, "instr");

        repository.save(task1);
        repository.save(task2);
        repository.save(task3);
        //when
        Page<TaskEntity> result = repository.findByOwnerId(100L, PageRequest.of(0, 10));

        //then
        assertEquals(result.getTotalElements(), 2L);
        assertThat(result.getContent()).extracting(TaskEntity::getOwnerId).containsOnly(100L);
        assertThat(result.getContent()).extracting(TaskEntity::isDeleted).containsOnly(false);
        //audit fields
        assertThat(result.getContent()).extracting(TaskEntity::getCreatedBy).containsOnly(111L);
        assertThat(result.getContent()).extracting(TaskEntity::getLastModifiedBy).containsOnly(111L);
        assertNotNull(result.getContent().get(0).getCreatedAt());
    }
}
