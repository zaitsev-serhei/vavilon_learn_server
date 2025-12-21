package com.language_practice_server.server_demo.db.repository;

import com.language_practice_server.server_demo.db.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepositoryJpa extends JpaRepository<RefreshTokenEntity, Long> {
    @Query(value = "SELECT rt.user_id FROM refresh_tokens rt WHERE token_hash=:hash", nativeQuery = true)
    Optional<Long> findUserIdByRefreshTokenHash(@Param("hash") String tokenHash);

    List<RefreshTokenEntity> findByUserIdAndRevokedFalse(Long userId);

    Optional<RefreshTokenEntity> findByRefreshTokenHash(String hash);

    void deleteByUserId(Long userId);
}
