package com.language_practice_server.server_demo.service.impl;

import com.language_practice_server.server_demo.domain.model.Person;
import com.language_practice_server.server_demo.domain.repository.PersonRepository;
import com.language_practice_server.server_demo.service.PersonService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person savePerson(Person person) {
//        personRepository.findPersonByEmail(person.getEmail())
//                .ifPresent(p -> {throw new RuntimeException("Person already exists");});

        return personRepository.savePerson(person);
    }

    @Override
    public Optional<Person> findPersonById(Long id) {
        return personRepository.findPersonById(id);
    }

//    @Override
//    public List<Person> findAllPersons() {
//        personRepository.findall
//        return List.of();
//    }

    @Override
    public void deletePersonById(Long id) {
        personRepository.deletePersonById(id);
    }
}
