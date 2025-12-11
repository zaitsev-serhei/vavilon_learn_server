package com.language_practice_server.server_demo.web.controller;

import com.language_practice_server.server_demo.domain.model.Assignment;
import com.language_practice_server.server_demo.mapper.AssignmentDtoMapper;
import com.language_practice_server.server_demo.service.AssignmentService;
import com.language_practice_server.server_demo.web.dto.AssignmentDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final AssignmentDtoMapper mapper;

    public AssignmentController(AssignmentService assignmentService, AssignmentDtoMapper mapper) {
        this.assignmentService = assignmentService;
        this.mapper = mapper;
    }

    @PostMapping("/assignments")
    public ResponseEntity<AssignmentDto> createAssignment(@RequestBody @Valid AssignmentDto assignmentDto){
        Assignment assignment= mapper.toDomain(assignmentDto);
        return ResponseEntity.ok(mapper.toDto(assignmentService.createAssignment(assignment)));
    }
    @GetMapping("assignments/{id}")
    public ResponseEntity<AssignmentDto> getAssignmentById(@PathVariable Long id){
        return ResponseEntity.ok(mapper.toDto(assignmentService.findById(id)));
    }
}
