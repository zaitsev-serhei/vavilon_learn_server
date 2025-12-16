package com.language_practice_server.server_demo.auth_service.domain.model;

import java.time.Instant;
import java.util.Objects;

public class RefreshToken {
    private Long id;
    private Long userId;
    private String refreshTokenHash;
    private Instant issuedAt;
    private Instant expiresAt;
    private String provider;
    private String providerRefreshTokenHash;
    private boolean revoked;

    public RefreshToken() {
    }

    public RefreshToken(Long id,
                        Long userId,
                        String tokenHash,
                        Instant issuedAt,
                        Instant expiresAt,
                        String provider,
                        String providerTokenHash,
                        boolean revoked) {
        this.id = id;
        this.userId = userId;
        this.refreshTokenHash = tokenHash;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.provider = provider;
        this.providerRefreshTokenHash = providerTokenHash;
        this.revoked = revoked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRefreshTokenHash() {
        return refreshTokenHash;
    }

    public void setRefreshTokenHash(String refreshTokenHash) {
        this.refreshTokenHash = refreshTokenHash;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderRefreshTokenHash() {
        return providerRefreshTokenHash;
    }

    public void setProviderRefreshTokenHash(String providerRefreshTokenHash) {
        this.providerRefreshTokenHash = providerRefreshTokenHash;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return getId().equals(that.getId()) &&
                getUserId().equals(that.getUserId()) &&
                getRefreshTokenHash().equals(that.getRefreshTokenHash());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId(), getRefreshTokenHash());
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "id=" + id +
                ", userId=" + userId +
                ", tokenHash='" + refreshTokenHash + '\'' +
                ", issuedAt=" + issuedAt +
                ", expiresAt=" + expiresAt +
                ", provider='" + provider + '\'' +
                ", providerTokenHash='" + providerRefreshTokenHash + '\'' +
                ", revoked=" + revoked +
                '}';
    }
}
