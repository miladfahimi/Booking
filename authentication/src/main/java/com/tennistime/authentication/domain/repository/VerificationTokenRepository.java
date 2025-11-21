package com.tennistime.authentication.domain.repository;

import com.tennistime.authentication.domain.model.User;
import com.tennistime.authentication.domain.model.VerificationToken;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository {
    Optional<VerificationToken> findByToken(String token);
    void delete(VerificationToken verificationToken);
    VerificationToken save(VerificationToken verificationToken);
    Optional<VerificationToken> findByUser(User user);
    Optional<VerificationToken> findByUserAndUsedFalse(User user);
}
