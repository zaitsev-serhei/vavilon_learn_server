package com.language_practice_server.server_demo.user_service.service;


import com.language_practice_server.server_demo.user_service.domain.model.User;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    User updateUser(User user);

    Optional<User> findUserById(Long id);

    Optional<User> findByEmail(String email);

    void deleteUserById(Long id);
}
