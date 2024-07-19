package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository {
    Optional<VerificationToken> findByToken(String token);
    void deleteByToken(String token);
    VerificationToken save(VerificationToken verificationToken); // Add the save method
}
