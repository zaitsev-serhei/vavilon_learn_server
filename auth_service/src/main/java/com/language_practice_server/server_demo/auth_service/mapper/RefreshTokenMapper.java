package com.language_practice_server.server_demo.auth_service.mapper;

import com.language_practice_server.server_demo.auth_service.db.entity.RefreshTokenEntity;
import com.language_practice_server.server_demo.auth_service.domain.model.RefreshToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    RefreshToken toDomain(RefreshTokenEntity entity);

    RefreshTokenEntity toEntity(RefreshToken token);
}
