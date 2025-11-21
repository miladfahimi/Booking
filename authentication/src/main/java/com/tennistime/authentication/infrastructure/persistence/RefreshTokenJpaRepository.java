package com.tennistime.authentication.infrastructure.persistence;

import com.tennistime.authentication.domain.model.RefreshToken;
import com.tennistime.authentication.domain.model.User;
import com.tennistime.authentication.domain.repository.RefreshTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, UUID>, RefreshTokenRepository {
    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByUser(User user);
}
