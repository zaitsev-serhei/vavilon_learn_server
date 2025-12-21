package com.language_practice_server.server_demo.user_service.mapper;


import com.language_practice_server.server_demo.user_service.db.entity.UserEntity;
import com.language_practice_server.server_demo.user_service.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface UserMapper {
    User toModel(UserEntity userEntity);
    UserEntity toEntity(User user);
}
