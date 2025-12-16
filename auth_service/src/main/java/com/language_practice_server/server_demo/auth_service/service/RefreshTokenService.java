package com.language_practice_server.server_demo.auth_service.service;

import com.language_practice_server.server_demo.auth_service.dto.UserDto;
import java.util.Optional;

public interface RefreshTokenService {
    String createRefreshTokenForUser(Long userId, String provider, String providerRefreshToken);

    Optional<UserDto> validateTokenAndGetUserByRefreshToken(String plainToken);

    String rotateRefreshToken(String oldPlainToken, Long userId);

    Optional<UserDto> checkExpiredAndGetUserToRotate(String plainToken);

    void revokeRefreshToken(String plainToken);
}
