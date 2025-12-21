package com.language_practice_server.server_demo.user_service.service.impl;


import com.language_practice_server.server_demo.user_service.domain.model.Person;
import com.language_practice_server.server_demo.user_service.domain.repository.PersonRepository;
import com.language_practice_server.server_demo.user_service.service.PersonService;
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
