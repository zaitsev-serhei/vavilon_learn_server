package com.language_practice_server.server_demo.user_service.domain.repository;


import com.language_practice_server.server_demo.user_service.domain.model.User;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository {
    Optional<User> findUserById(Long id);

    Optional<User> findUserByUserName(String userName);

    Optional<User> findByEmail(String email);

    User saveUser(User user);

    void deleteUserById(Long id);
}
