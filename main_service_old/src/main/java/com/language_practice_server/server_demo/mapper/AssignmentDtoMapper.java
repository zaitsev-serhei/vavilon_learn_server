package com.language_practice_server.server_demo.mapper;

import com.language_practice_server.server_demo.domain.model.Assignment;
import com.language_practice_server.server_demo.web.dto.AssignmentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssignmentDtoMapper {
    Assignment toDomain(AssignmentDto dto);

    AssignmentDto toDto(Assignment assignment);
}
