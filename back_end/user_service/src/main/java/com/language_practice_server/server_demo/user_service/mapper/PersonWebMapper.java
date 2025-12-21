package com.language_practice_server.server_demo.user_service.mapper;


import com.language_practice_server.server_demo.user_service.domain.model.Person;
import com.language_practice_server.server_demo.user_service.web.dto.PersonDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonWebMapper {
    Person toModel(PersonDto  personDto);
    PersonDto toDto(Person person);
}
