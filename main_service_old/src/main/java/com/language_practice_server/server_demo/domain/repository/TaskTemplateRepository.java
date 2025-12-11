package com.language_practice_server.server_demo.domain.repository;

import com.language_practice_server.server_demo.domain.model.TaskTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskTemplateRepository {
    TaskTemplate save(TaskTemplate template);

    TaskTemplate update(TaskTemplate template);

    void delete(Long templateId);

    Optional<TaskTemplate> findTaskTemplateById(Long templateId);

    Page<TaskTemplate> findAllTaskTemplateByCreatorId(Long creatorId, Pageable pageable);

    Page<TaskTemplate> findActiveTaskTemplateByCreatorId(Long creatorId, Pageable pageable);

    Page<TaskTemplate> findAllTaskTemplate(Pageable pageable);
}
