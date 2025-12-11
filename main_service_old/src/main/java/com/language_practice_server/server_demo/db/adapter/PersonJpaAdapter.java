package com.language_practice_server.server_demo.db.adapter;

import com.language_practice_server.server_demo.db.entity.PersonEntity;
import com.language_practice_server.server_demo.db.repository.PersonRepositoryJpa;
import com.language_practice_server.server_demo.domain.model.Person;
import com.language_practice_server.server_demo.domain.repository.PersonRepository;
import com.language_practice_server.server_demo.mapper.PersonMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PersonJpaAdapter implements PersonRepository {
    private final PersonRepositoryJpa personRepositoryJpa;
    private final PersonMapper personMapper;

    public PersonJpaAdapter(PersonRepositoryJpa personRepositoryJpa, PersonMapper personMapper) {
        this.personRepositoryJpa = personRepositoryJpa;
        this.personMapper = personMapper;
    }

    @Override
    public Optional<Person> findPersonById(Long id) {
        return personRepositoryJpa.findById(id).map(personMapper::toModel);
    }

    @Override
    public Person savePerson(Person person) {
        PersonEntity personEntity = personRepositoryJpa.save(personMapper.toEntity(person));
        return personMapper.toModel(personEntity);
    }

    @Override
    public void deletePersonById(Long id) {
        personRepositoryJpa.deleteById(id);
    }
}
