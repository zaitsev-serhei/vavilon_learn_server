package com.language_practice_server.server_demo.user_service.mapper;


import com.language_practice_server.server_demo.user_service.db.entity.PersonEntity;
import com.language_practice_server.server_demo.user_service.domain.model.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    Person toModel(PersonEntity personEntity);
    PersonEntity toEntity(Person person);
}
