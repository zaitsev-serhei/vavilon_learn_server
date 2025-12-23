package com.language_practice_server.server_demo.mapper;

import com.language_practice_server.server_demo.db.entity.AssignmentEntity;
import com.language_practice_server.server_demo.domain.model.Assignment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {
    Assignment toDomain(AssignmentEntity entity);

    AssignmentEntity toEntity(Assignment assignment);
}
