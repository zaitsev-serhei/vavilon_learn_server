package com.language_practice_server.server_demo.user_service.mapper;


import com.language_practice_server.server_demo.user_service.domain.model.User;
import com.language_practice_server.server_demo.user_service.web.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PersonWebMapper.class})
public interface UserWebMapper {
    User toModel(UserDto  userDto);
    UserDto toDto(User user);
}
