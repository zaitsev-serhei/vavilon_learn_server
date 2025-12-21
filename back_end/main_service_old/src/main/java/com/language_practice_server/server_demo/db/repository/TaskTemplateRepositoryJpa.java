package com.language_practice_server.server_demo.db.repository;

import com.language_practice_server.server_demo.db.entity.TaskTemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTemplateRepositoryJpa extends JpaRepository<TaskTemplateEntity, Long> {
    Page<TaskTemplateEntity> findByOwnerId(Long creatorId, Pageable page);

    Page<TaskTemplateEntity> findByOwnerIdAndDeletedFalse(Long creatorId, Pageable page);
}
