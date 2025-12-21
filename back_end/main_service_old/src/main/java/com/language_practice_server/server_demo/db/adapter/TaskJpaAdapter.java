package com.language_practice_server.server_demo.db.adapter;

import com.language_practice_server.server_demo.db.entity.TaskEntity;
import com.language_practice_server.server_demo.db.repository.TaskRepositoryJpa;
import com.language_practice_server.server_demo.domain.model.Task;
import com.language_practice_server.server_demo.domain.repository.TaskRepository;
import com.language_practice_server.server_demo.mapper.TaskMapper;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public class TaskJpaAdapter implements TaskRepository {
    private final TaskRepositoryJpa repositoryJpa;
    private final TaskMapper taskMapper;

    public TaskJpaAdapter(TaskRepositoryJpa repositoryJpa,
                          TaskMapper mapper) {
        this.repositoryJpa = repositoryJpa;
        this.taskMapper = mapper;
    }

    @Override
    public Task saveTask(Task task) {
        TaskEntity entity = taskMapper.toEntity(task);
        TaskEntity saved = repositoryJpa.save(entity);
        return taskMapper.toDomain(saved);
    }

    @Override
    public Task updateTask(Task task) {
        TaskEntity entity = taskMapper.toEntity(task);
        return taskMapper.toDomain(repositoryJpa.save(entity));
    }

    @Override
    public void delete(Long id) {
        TaskEntity entityToDelete = repositoryJpa.findById(id).get();
        entityToDelete.setDeleted(true);
        repositoryJpa.save(entityToDelete);
    }

    @Override
    public Optional<Task> findById(Long taskId) {
        return repositoryJpa.findById(taskId).map(taskMapper::toDomain);
    }

    @Override
    public Page<Task> findActiveTaskByCreatorId(Long creatorId, Pageable pageable) {
        return repositoryJpa.findByOwnerIdAndDeletedFalse(creatorId, pageable).map(taskMapper::toDomain);
    }

    @Override
    public Page<Task> findAllTaskByCreatorId(Long creatorId, Pageable pageable) {
        return repositoryJpa.findByOwnerId(creatorId, pageable).map(taskMapper::toDomain);
    }

    @Override
    public Page<Task> findAllTask(Pageable pageable) {
        return repositoryJpa.findAll(pageable).map(taskMapper::toDomain);
    }
}
