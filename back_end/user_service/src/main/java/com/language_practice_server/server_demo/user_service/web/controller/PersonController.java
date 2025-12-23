package com.language_practice_server.server_demo.user_service.web.controller;


import com.language_practice_server.server_demo.user_service.mapper.PersonWebMapper;
import com.language_practice_server.server_demo.user_service.service.PersonService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;
    private final PersonWebMapper personWebMapper;

    public PersonController(PersonService personController, PersonWebMapper personWebMapper) {
        this.personService = personController;
        this.personWebMapper = personWebMapper;
    }

    //methods
}
