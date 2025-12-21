package com.language_practice_server.server_demo.auth_service.client;

import com.language_practice_server.server_demo.auth_service.dto.UserDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/api/users/{userId}")
    UserDto findUserById(@PathVariable Long userId);

    @GetMapping("/api/users/{email}")
    UserDto findUserByEmail(@PathVariable String email);

    @PostMapping("/create")
    UserDto createUser(@RequestBody UserDto user);
}
