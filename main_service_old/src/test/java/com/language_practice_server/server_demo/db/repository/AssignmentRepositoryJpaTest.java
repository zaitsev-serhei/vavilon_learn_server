package com.language_practice_server.server_demo.db.repository;

import com.language_practice_server.server_demo.common.enums.AssignmentStatus;
import com.language_practice_server.server_demo.config.TestAuditorConfig;
import com.language_practice_server.server_demo.db.entity.AssignmentEntity;
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
public class AssignmentRepositoryJpaTest {
    @Autowired
    private AssignmentRepositoryJpa repository;

    @Test
    public void findByOwnerIdReturnsPageAndNotDeleted() {
        AssignmentEntity assignment1 = new AssignmentEntity(null, 100L, 200L, 300L, AssignmentStatus.NEW);
        AssignmentEntity assignment2 = new AssignmentEntity(null, 100L, 200L, 300L, AssignmentStatus.IN_PROCESS);
        AssignmentEntity assignment3 = new AssignmentEntity(null, 200L, 200L, 300L, AssignmentStatus.ASSIGNED);

        repository.save(assignment1);
        repository.save(assignment2);
        repository.save(assignment3);

        Page<AssignmentEntity> result = repository.findByOwnerIdAndDeletedFalse(100L, PageRequest.of(0, 10));

        assertEquals(result.getTotalElements(), 2L);
        assertThat(result.getContent()).extracting(AssignmentEntity::getOwnerId).containsOnly(100L);
        assertThat(result.getContent()).extracting(AssignmentEntity::isDeleted).containsOnly(false);

        assertThat(result.getContent()).extracting(AssignmentEntity::getCreatedBy).containsOnly(111L);
        assertThat(result.getContent()).extracting(AssignmentEntity::getLastModifiedBy).containsOnly(111L);
        assertNotNull(result.getContent().get(0).getCreatedAt());
    }

    @Test
    public void findByAssigneeIdReturnPage() {
        AssignmentEntity assignment1 = new AssignmentEntity(null, 100L, 200L, 300L, AssignmentStatus.NEW);
        AssignmentEntity assignment2 = new AssignmentEntity(null, 100L, 200L, 300L, AssignmentStatus.IN_PROCESS);
        AssignmentEntity assignment3 = new AssignmentEntity(null, 200L, 200L, 300L, AssignmentStatus.ASSIGNED);

        repository.save(assignment1);
        repository.save(assignment2);
        repository.save(assignment3);

        Page<AssignmentEntity> result = repository.findByAssigneeId(200L, PageRequest.of(0, 10));
        assertEquals(result.getTotalElements(), 3L);
        assertThat(result.getContent()).extracting(AssignmentEntity::getAssigneeId).containsOnly(200L);
        assertThat(result.getContent()).extracting(AssignmentEntity::isDeleted).containsOnly(false);

        assertThat(result.getContent()).extracting(AssignmentEntity::getCreatedBy).containsOnly(111L);
        assertThat(result.getContent()).extracting(AssignmentEntity::getLastModifiedBy).containsOnly(111L);
        assertNotNull(result.getContent().get(0).getCreatedAt());
    }
}
