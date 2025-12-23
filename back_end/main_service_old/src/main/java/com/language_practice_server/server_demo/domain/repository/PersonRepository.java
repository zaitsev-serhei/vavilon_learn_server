package com.language_practice_server.server_demo.domain.repository;

import com.language_practice_server.server_demo.domain.model.Person;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository {
    Optional<Person> findPersonById(Long id);
    Person savePerson(Person person);
    void deletePersonById(Long id);
}
