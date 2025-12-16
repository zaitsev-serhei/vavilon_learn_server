package com.language_practice_server.server_demo.auth_service.dto;

public class ServiceTokenResponse {
    private final String token;
    private final long expiresAt;

    public ServiceTokenResponse(String token, long expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public long getExpiresAt() {
        return expiresAt;
    }
}
