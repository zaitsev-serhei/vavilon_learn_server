package com.language_practice_server.server_demo.db.adapter;

import com.language_practice_server.server_demo.db.entity.TaskTemplateEntity;
import com.language_practice_server.server_demo.db.repository.TaskTemplateRepositoryJpa;
import com.language_practice_server.server_demo.domain.model.TaskTemplate;
import com.language_practice_server.server_demo.domain.repository.TaskTemplateRepository;
import com.language_practice_server.server_demo.mapper.TaskTemplateMapper;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public class TaskTemplateJpaAdapter implements TaskTemplateRepository {
    private final TaskTemplateRepositoryJpa repositoryJpa;
    private final TaskTemplateMapper templateMapper;

    public TaskTemplateJpaAdapter(TaskTemplateRepositoryJpa repositoryJpa,
                                  TaskTemplateMapper mapper) {
        this.repositoryJpa = repositoryJpa;
        this.templateMapper = mapper;
    }

    @Override
    public TaskTemplate save(TaskTemplate template) {
        TaskTemplateEntity entity = templateMapper.toEntity(template);
        return templateMapper.toDomain(repositoryJpa.save(entity));
    }

    @Override
    public TaskTemplate update(TaskTemplate template) {
        TaskTemplateEntity entity = templateMapper.toEntity(template);
        return templateMapper.toDomain(repositoryJpa.save(entity));
    }

    @Override
    public void delete(Long templateId) {
        TaskTemplateEntity entity = repositoryJpa.findById(templateId).get();
        entity.setDeleted(true);
        repositoryJpa.save(entity);
    }

    @Override
    public Optional<TaskTemplate> findTaskTemplateById(Long templateId) {
        return repositoryJpa.findById(templateId).map(templateMapper::toDomain);
    }

    @Override
    public Page<TaskTemplate> findAllTaskTemplateByCreatorId(Long creatorId, Pageable pageable) {
        return repositoryJpa.findByOwnerId(creatorId, pageable).map(templateMapper::toDomain);
    }

    @Override
    public Page<TaskTemplate> findActiveTaskTemplateByCreatorId(Long creatorId, Pageable pageable) {
        return repositoryJpa.findByOwnerIdAndDeletedFalse(creatorId, pageable).map(templateMapper::toDomain);
    }

    @Override
    public Page<TaskTemplate> findAllTaskTemplate(Pageable page) {
        return repositoryJpa.findAll(page).map(templateMapper::toDomain);
    }
}
