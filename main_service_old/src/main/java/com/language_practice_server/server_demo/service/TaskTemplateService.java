package com.language_practice_server.server_demo.service;

import com.language_practice_server.server_demo.domain.model.TaskTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskTemplateService {
    TaskTemplate createTemplate(TaskTemplate template);

    TaskTemplate findTemplateById(Long templateId);

    TaskTemplate updateTemplate(TaskTemplate template);

    void deleteTemplate(Long templateId);

    Page<TaskTemplate> findAllTaskTemplateByOwnerId(Long creatorId, Pageable pageable);
}
