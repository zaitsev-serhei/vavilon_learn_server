package com.language_practice_server.server_demo.auth_service.dto;

public class RefreshResponse {
    private final String token;

    public RefreshResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
