package com.language_practice_server.server_demo.service.impl;

import com.language_practice_server.server_demo.common.EventTopics;
import com.language_practice_server.server_demo.domain.model.Task;
import com.language_practice_server.server_demo.domain.repository.TaskRepository;
import com.language_practice_server.server_demo.kafka.KafkaEventProducer;
import com.language_practice_server.server_demo.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vavilonLearn.contracts.events.TaskCreatedEvent;

import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {
    private final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;
    private final KafkaEventProducer eventProducer;

    public TaskServiceImpl(TaskRepository taskRepository, KafkaEventProducer eventProducer) {
        this.taskRepository = taskRepository;
        this.eventProducer = eventProducer;
    }

    @Override
    @Transactional
    public Task createTask(Task task) {
        if (task.getTaskTemplateId() == null || task.getOwnerId() == null) {
            logger.error("Task creation failed. Missing fields in task: {}", task.toString());
            throw new IllegalArgumentException("Task Template isn`t defined");
        }
        Task saved = taskRepository.saveTask(task);
        if (saved.getId() != null) {
            TaskCreatedEvent event = new TaskCreatedEvent(
                    UUID.randomUUID().toString(),
                    saved.getId().toString(),
                    saved.getOwnerId().toString(),
                    UUID.randomUUID().toString(),
                    "New Task",
                    "New Task description",
                    saved.getCreatedAt());
            System.out.println(event.toString());
            //TODO: bug identified when Kafka server is down. Will be fixed in next patch :)
            eventProducer.publish(EventTopics.TASK_CREATED, saved.getOwnerId().toString(), event);
            logger.debug("New task is created in DB. Producing Kafka event: {}", event.getEventId());
        }
        logger.debug("saved task: {}",saved.toString());
        return saved;
    }

    @Override
    @Transactional
    public Task updateTask(Task task) {
        if (task.getTaskTemplateId() == null) {
            logger.error("Task creation failed. Missing templateId in task: {}", task.toString());
            throw new IllegalArgumentException("Task Template isn`t defined");
        }
        logger.debug("Updating task in DB. Task to be updated: {}", task.toString());
        return taskRepository.updateTask(task);
    }

    @Override
    @Transactional
    public void delete(Long taskId) {
        logger.debug("Deleting task with Id: {}", taskId);
        taskRepository.delete(taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public Task findTaskById(Long taskId) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task id isn`t defined");
        }
        return taskRepository.findById(taskId).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> findAllTasksByOwnerId(Long creatorId, Pageable pageable) {
        if (creatorId == null) {
            throw new IllegalArgumentException("Task id isn`t defined");
        }
        return taskRepository.findAllTaskByCreatorId(creatorId, pageable);
    }
}
