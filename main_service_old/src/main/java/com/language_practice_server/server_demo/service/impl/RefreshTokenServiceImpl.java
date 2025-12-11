package com.language_practice_server.server_demo.service.impl;

import com.language_practice_server.server_demo.domain.model.RefreshToken;
import com.language_practice_server.server_demo.domain.model.User;
import com.language_practice_server.server_demo.domain.repository.RefreshTokenRepository;
import com.language_practice_server.server_demo.service.RefreshTokenService;
import com.language_practice_server.server_demo.service.UserService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);
    private final UserService userService;
    private final RefreshTokenRepository tokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    @Value("${refresh.cookie.max-age}")
    private Long tokenMaxAgeSeconds;
    @Value("${security.jwt.secret-key}")
    private String jwtSecret;

    public RefreshTokenServiceImpl(UserService userService, RefreshTokenRepository tokenRepository) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
    }

    /*
        creates server-side refresh token for a user and stores it`s hash in the DB
        - returns plain token to be set in cookies for a user
     */
    @Override
    public String createRefreshTokenForUser(Long userId,
                                            String provider,
                                            String providerRefreshToken) {
        byte[] rnd = new byte[64];
        secureRandom.nextBytes(rnd);
        String plainToken = Base64.getUrlEncoder().withoutPadding().encodeToString(rnd);
        String tokenHash = hashToken(plainToken);
        logger.info("New refresh token is created for userId: {} with hash:{}", userId, tokenHash);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setRefreshTokenHash(tokenHash);
        refreshToken.setProvider(provider);
        refreshToken.setProviderRefreshTokenHash(hashToken(providerRefreshToken));
        refreshToken.setExpiresAt(Instant.now().plusSeconds(tokenMaxAgeSeconds));
        refreshToken.setIssuedAt(Instant.now());
        refreshToken.setRevoked(false);
        tokenRepository.create(refreshToken);
        logger.debug("Refresh token for userId:{} is saved in the DB with details -->{}", userId, refreshToken);
        return plainToken;
    }

    @Override
    public Optional<User> validateTokenAndGetUserByRefreshToken(String plainToken) {
        String tokenHash = hashToken(plainToken);
        logger.info("Validating refresh token with hash = {}", tokenHash);
        Optional<RefreshToken> rt = tokenRepository.findByTokenHash(tokenHash);
        if (rt.isEmpty()) {
            logger.debug("Refresh token with hash = {} was not found in the DB", tokenHash);
            return Optional.empty();
        }
        RefreshToken storedToken = rt.get();
        if (storedToken.isRevoked()) {
            logger.debug("Refresh token with hash = {} was revoked and can`t be used", tokenHash);
            return Optional.empty();
        }
        if (storedToken.getExpiresAt() != null && storedToken.getExpiresAt().isBefore(Instant.now())) {
            logger.debug("Refresh token with hash = {} is expired and must be rotated", tokenHash);
            return Optional.empty();
        }
        return userService.findUserById(storedToken.getUserId());
    }

    @Override
    public Optional<User> checkExpiredAndGetUserToRotate(String plainToken) {
        String tokenHash = hashToken(plainToken);
        Optional<RefreshToken> rt = tokenRepository.findByTokenHash(tokenHash);
        if (rt.isEmpty()) {
            logger.debug("Refresh token with hash = {} was not found in the DB", tokenHash);
            return Optional.empty();
        }
        RefreshToken storedToken = rt.get();
        if (storedToken.getExpiresAt() != null && storedToken.getExpiresAt().isBefore(Instant.now()) && !storedToken.isRevoked()) {
            logger.debug("Refresh token with hash = {} is expired and must be rotated", tokenHash);
            userService.findUserById(storedToken.getUserId());
        }
        return Optional.empty();
    }

    @Override
    public String rotateRefreshToken(String oldPlainToken, Long userId) {
        logger.info("Rotating refresh token for userId: {}", userId);
        String oldTokenHash = hashToken(oldPlainToken);
        Optional<RefreshToken> oldToken = tokenRepository.findByTokenHash(oldTokenHash);
        if (oldToken.isPresent()) {
            RefreshToken rt = oldToken.get();
            rt.setRevoked(true);
            tokenRepository.update(rt);
            logger.debug("Refresh token with id= {} is set to revoked and no longer in use", rt.getId());
        }
        byte[] rnd = new byte[64];
        secureRandom.nextBytes(rnd);
        String newPlain = Base64.getUrlEncoder().withoutPadding().encodeToString(rnd);
        String newHash = hashToken(newPlain);
        RefreshToken refreshToken = new RefreshToken();
        logger.info("New refresh token is created for userId: {} with hash:{}", userId, newHash);
        refreshToken.setUserId(userId);
        refreshToken.setRefreshTokenHash(newHash);
        refreshToken.setExpiresAt(Instant.now().plusSeconds(tokenMaxAgeSeconds));
        refreshToken.setIssuedAt(Instant.now());
        refreshToken.setRevoked(false);
        tokenRepository.create(refreshToken);
        logger.debug("Refresh token for userId:{} is saved in the DB with details -->{}", userId, refreshToken);
        return newPlain;
    }

    @Override
    public void revokeRefreshToken(String plainToken) {
        logger.debug("Revoking the token");
        String hash = hashToken(plainToken);
        tokenRepository.findByTokenHash(hash).ifPresent(rt -> {
            rt.setRevoked(true);
            tokenRepository.update(rt);
        });
    }

    private String hashToken(String plain) {
        logger.debug("Hashing the token");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(jwtSecret.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest(plain.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to hash token", ex);
        }
    }
}
