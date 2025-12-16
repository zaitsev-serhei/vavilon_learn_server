package com.language_practice_server.server_demo.auth_service.db.adapter;

import com.language_practice_server.server_demo.auth_service.db.entity.RefreshTokenEntity;
import com.language_practice_server.server_demo.auth_service.db.repository.RefreshTokenRepositoryJpa;
import com.language_practice_server.server_demo.auth_service.domain.model.RefreshToken;
import com.language_practice_server.server_demo.auth_service.domain.repository.RefreshTokenRepository;
import com.language_practice_server.server_demo.auth_service.mapper.RefreshTokenMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RefreshTokenJpaAdapter implements RefreshTokenRepository {
    private final RefreshTokenRepositoryJpa repositoryJpa;
    private final RefreshTokenMapper mapper;

    public RefreshTokenJpaAdapter(RefreshTokenRepositoryJpa repositoryJpa, RefreshTokenMapper mapper) {
        this.repositoryJpa = repositoryJpa;
        this.mapper = mapper;
    }

    @Override
    public RefreshToken create(RefreshToken token) {
        RefreshTokenEntity entity = mapper.toEntity(token);
        return mapper.toDomain(repositoryJpa.save(entity));
    }

    @Override
    public RefreshToken update(RefreshToken token) {
        return mapper.toDomain(repositoryJpa.save(mapper.toEntity(token)));
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String hash) {
        return repositoryJpa.findByRefreshTokenHash(hash).map(mapper::toDomain);
    }

    @Override
    public List<RefreshToken> findByUserId(Long userId) {
        return repositoryJpa.findByUserIdAndRevokedFalse(userId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Long> findUserIdByTokenHash(String token) {
        return repositoryJpa.findUserIdByRefreshTokenHash(token);
    }

    @Override
    public void deleteByUserId(Long userId) {
        repositoryJpa.deleteByUserId(userId);
    }
}
