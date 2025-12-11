package com.language_practice_server.server_demo.mapper;

import com.language_practice_server.server_demo.db.entity.TaskEntity;
import com.language_practice_server.server_demo.domain.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toDomain(TaskEntity entity);

    TaskEntity toEntity(Task task);
}
