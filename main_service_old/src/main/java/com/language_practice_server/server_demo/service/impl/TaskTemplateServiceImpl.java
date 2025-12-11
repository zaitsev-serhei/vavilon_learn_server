package com.language_practice_server.server_demo.service.impl;

import com.language_practice_server.server_demo.domain.model.TaskTemplate;
import com.language_practice_server.server_demo.domain.repository.TaskTemplateRepository;
import com.language_practice_server.server_demo.service.TaskTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskTemplateServiceImpl implements TaskTemplateService {
    private final TaskTemplateRepository templateRepository;

    public TaskTemplateServiceImpl(TaskTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    @Transactional
    public TaskTemplate createTemplate(TaskTemplate template) {
        if (template.getTitle() == null || template.getTitle().isBlank() || template.getOwnerId() == null) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        return templateRepository.save(template);
    }

    @Override
    @Transactional
    public TaskTemplate updateTemplate(TaskTemplate template) {
        if (template.getTitle() == null || template.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        return templateRepository.save(template);
    }

    @Override
    @Transactional
    public void deleteTemplate(Long templateId) {
        templateRepository.delete(templateId);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskTemplate findTemplateById(Long templateId) {
        return templateRepository.findTaskTemplateById(templateId).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskTemplate> findAllTaskTemplateByOwnerId(Long creatorId, Pageable pageable) {
        return templateRepository.findAllTaskTemplateByCreatorId(creatorId, pageable);
    }
}
