package com.language_practice_server.server_demo.web.security;


import com.language_practice_server.server_demo.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Custom implementation of Spring's UserDetails.
 * Wraps UserEntity to provide user information (username, password, roles) to Spring Security.
 */

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User userEntity) {
        this.user = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //todo: add role parsing
        return List.of(); // currently returning empty list, later this method will return the user's roles list.
    }

    @Override
    public String getPassword() {
        return "user.getPassword()";
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }
}
