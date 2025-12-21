package com.language_practice_server.server_demo.mapper;

import com.language_practice_server.server_demo.db.entity.RefreshTokenEntity;
import com.language_practice_server.server_demo.domain.model.RefreshToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    RefreshToken toDomain(RefreshTokenEntity entity);

    RefreshTokenEntity toEntity(RefreshToken token);
}
