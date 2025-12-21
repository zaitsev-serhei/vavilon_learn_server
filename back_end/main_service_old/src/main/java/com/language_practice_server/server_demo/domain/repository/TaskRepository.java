package com.language_practice_server.server_demo.domain.repository;

import com.language_practice_server.server_demo.domain.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskRepository {
    Task saveTask(Task task);

    Task updateTask(Task task);

    void delete(Long id);

    Optional<Task> findById(Long taskId);

    Page<Task> findActiveTaskByCreatorId(Long creatorId, Pageable pageable);

    Page<Task> findAllTask(Pageable pageable);

    Page<Task> findAllTaskByCreatorId(Long creatorId, Pageable pageable);

}
