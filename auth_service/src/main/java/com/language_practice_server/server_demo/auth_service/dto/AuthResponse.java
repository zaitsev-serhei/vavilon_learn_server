package com.language_practice_server.server_demo.auth_service.dto;

public class AuthResponse {
    private final UserDto user;
    private final String accessToken;

    public AuthResponse(UserDto user, String accessToken) {
        this.user = user;
        this.accessToken = accessToken;
    }

    public UserDto getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
