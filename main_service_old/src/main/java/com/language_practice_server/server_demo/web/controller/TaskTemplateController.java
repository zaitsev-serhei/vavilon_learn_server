package com.language_practice_server.server_demo.web.controller;

import com.language_practice_server.server_demo.domain.model.TaskTemplate;
import com.language_practice_server.server_demo.mapper.TaskTemplateDtoMapper;
import com.language_practice_server.server_demo.service.TaskTemplateService;
import com.language_practice_server.server_demo.web.dto.TaskTemplateDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasktemplate")
public class TaskTemplateController {
    private final TaskTemplateService templateService;
    private final TaskTemplateDtoMapper mapper;

    public TaskTemplateController(TaskTemplateService templateService,
                                  TaskTemplateDtoMapper mapper) {
        this.templateService = templateService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<TaskTemplateDto> createTemplate(@RequestBody @Valid TaskTemplateDto dto) {
        TaskTemplate template = mapper.toDomain(dto);
        return ResponseEntity.ok(mapper.toDto(templateService.createTemplate(template)));
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<TaskTemplateDto> getTemplateById(@PathVariable Long templateId) {
        return ResponseEntity.ok(mapper.toDto(templateService.findTemplateById(templateId)));
    }

    @DeleteMapping("/{templateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTemplate(@PathVariable Long templateId) {
        templateService.deleteTemplate(templateId);
    }
}
