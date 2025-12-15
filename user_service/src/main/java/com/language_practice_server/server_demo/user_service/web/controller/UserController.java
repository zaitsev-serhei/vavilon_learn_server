package com.language_practice_server.server_demo.user_service.web.controller;


import com.language_practice_server.server_demo.user_service.mapper.UserWebMapper;
import com.language_practice_server.server_demo.user_service.service.UserService;
import com.language_practice_server.server_demo.user_service.web.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final UserWebMapper userWebMapper;

    public UserController(UserService userService, UserWebMapper userWebMapper) {
        this.userService = userService;
        this.userWebMapper = userWebMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        return userService.findUserById(id)
                .map(userWebMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        var savedUser = userService.saveUser(userWebMapper.toModel(userDto));
        return ResponseEntity.ok(userWebMapper.toDto(savedUser));
    }
}
