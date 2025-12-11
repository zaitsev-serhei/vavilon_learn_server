package com.language_practice_server.server_demo.db.repository;

import com.language_practice_server.server_demo.db.entity.AssignmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepositoryJpa extends JpaRepository<AssignmentEntity, Long> {
    Page<AssignmentEntity> findByOwnerIdAndDeletedFalse(Long ownerId, Pageable page);

    Page<AssignmentEntity> findByAssigneeId(Long assigneeId, Pageable page);

}
