package com.language_practice_server.server_demo.mapper;

import com.language_practice_server.server_demo.domain.model.Task;
import com.language_practice_server.server_demo.web.dto.TaskDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskDtoMapper {
    Task toDomain(TaskDto dto);

    TaskDto toDto(Task task);
}
