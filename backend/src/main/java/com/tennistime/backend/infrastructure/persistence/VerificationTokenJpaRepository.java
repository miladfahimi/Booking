package com.tennistime.backend.infrastructure.repository;

import com.tennistime.backend.domain.model.VerificationToken;
import com.tennistime.backend.domain.repository.VerificationTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenJpaRepository extends VerificationTokenRepository, JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    void deleteByToken(String token);
}
