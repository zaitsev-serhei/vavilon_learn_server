package com.language_practice_server.server_demo.web.controller;

import com.language_practice_server.server_demo.domain.model.Task;
import com.language_practice_server.server_demo.mapper.TaskDtoMapper;
import com.language_practice_server.server_demo.service.TaskService;
import com.language_practice_server.server_demo.web.dto.TaskDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Tasks", description = "Endpoints for Task operations")
@RestController
@RequestMapping("tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskDtoMapper mapper;

    public TaskController(TaskService taskService, TaskDtoMapper mapper) {
        this.taskService = taskService;
        this.mapper = mapper;
    }

    @Operation(summary = "Create new Task", description = "Creates a task and returns DTO with an id of the item created")
    @ApiResponse(responseCode = "201", description = "Task Created")
    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Task DTO with valid fields"
                    , required = true)
            @RequestBody @Valid TaskDto dto) {
        Task task = mapper.toDomain(dto);
        return ResponseEntity.ok(mapper.toDto(taskService.createTask(task)));
    }

    @Operation(summary = "Find Task by id", description = "Return a task if found")
    @ApiResponse(responseCode = "201", description = "Find a task by id")
    @GetMapping("/findById/{taskId}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long taskId) {
        return ResponseEntity.ok(mapper.toDto(taskService.findTaskById(taskId)));
    }

    @Operation(summary = "Find Tasks by ownerId", description = "Returns a page of tasks created by owner. Page result is predefined if missing")
    @ApiResponse(responseCode = "201", description = "Find Tasks by ownerId")
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<Page<TaskDto>> getTaskByCreatorId(@PathVariable Long creatorId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Task> tasksPage = taskService.findAllTasksByOwnerId(creatorId, pageable);
        Page<TaskDto> dtoPage = tasksPage.map(mapper::toDto);
        if (dtoPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dtoPage);
    }

    @Operation(summary = "Delete a Task", description = "Marks the Task as Deleted but stores in DB")
    @ApiResponse(responseCode = "204", description = "Task Deleted")
    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long taskId) {
        taskService.delete(taskId);
    }
}
