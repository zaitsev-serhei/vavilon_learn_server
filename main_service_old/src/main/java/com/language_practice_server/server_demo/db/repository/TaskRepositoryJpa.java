package com.language_practice_server.server_demo.db.repository;

import com.language_practice_server.server_demo.db.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepositoryJpa extends JpaRepository<TaskEntity, Long> {
    Page<TaskEntity> findByOwnerId(Long creatorId, Pageable page);

    Page<TaskEntity> findByOwnerIdAndDeletedFalse(Long creatorId, Pageable page);
}
