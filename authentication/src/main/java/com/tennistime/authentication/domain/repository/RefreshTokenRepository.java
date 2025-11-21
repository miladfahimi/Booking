package com.tennistime.authentication.domain.repository;

import com.tennistime.authentication.domain.model.RefreshToken;
import com.tennistime.authentication.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findById(UUID id);

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByUser(User user);

    RefreshToken save(RefreshToken refreshToken);
}
