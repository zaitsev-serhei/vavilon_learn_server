package com.language_practice_server.server_demo.auth_service.domain.repository;

import com.language_practice_server.server_demo.auth_service.domain.model.RefreshToken;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken create(RefreshToken token);

    RefreshToken update(RefreshToken token);

    Optional<RefreshToken> findByTokenHash(String hash);

    List<RefreshToken> findByUserId(Long userId);

    Optional<Long> findUserIdByTokenHash(String token);

    void deleteByUserId(Long userId);
}
