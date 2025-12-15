package com.language_practice_server.server_demo.user_service.service;


import com.language_practice_server.server_demo.user_service.domain.model.Person;
import java.util.Optional;

public interface PersonService {
    Person savePerson(Person person);
    Optional<Person> findPersonById(Long id);
//    List<Person> findAllPersons();
    void deletePersonById(Long id);
}
