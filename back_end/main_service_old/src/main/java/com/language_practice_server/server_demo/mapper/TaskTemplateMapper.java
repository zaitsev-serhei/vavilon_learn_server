package com.language_practice_server.server_demo.mapper;

import com.language_practice_server.server_demo.db.entity.TaskTemplateEntity;
import com.language_practice_server.server_demo.domain.model.TaskTemplate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskTemplateMapper {
    TaskTemplate toDomain(TaskTemplateEntity entity);

    TaskTemplateEntity toEntity(TaskTemplate template);
}
