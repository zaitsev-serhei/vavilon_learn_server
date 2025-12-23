package com.language_practice_server.server_demo.mapper;

import com.language_practice_server.server_demo.domain.model.TaskTemplate;
import com.language_practice_server.server_demo.web.dto.TaskTemplateDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskTemplateDtoMapper {
    TaskTemplate toDomain(TaskTemplateDto dto);

    TaskTemplateDto toDto(TaskTemplate template);
}
