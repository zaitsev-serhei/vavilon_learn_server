package com.language_practice_server.server_demo.service;

import com.language_practice_server.server_demo.domain.model.User;

import java.util.Optional;

public interface RefreshTokenService {
    String createRefreshTokenForUser(Long userId, String provider, String providerRefreshToken);

    Optional<User> validateTokenAndGetUserByRefreshToken(String plainToken);

    String rotateRefreshToken(String oldPlainToken, Long userId);

    Optional<User> checkExpiredAndGetUserToRotate(String plainToken);

    void revokeRefreshToken(String plainToken);
}
