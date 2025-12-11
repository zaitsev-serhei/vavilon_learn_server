package com.language_practice_server.server_demo.service;

import com.language_practice_server.server_demo.domain.model.Person;

import java.util.Optional;

public interface PersonService {
    Person savePerson(Person person);
    Optional<Person> findPersonById(Long id);
//    List<Person> findAllPersons();
    void deletePersonById(Long id);
}
