package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.AppUser;
import com.tennistime.backend.domain.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository {
    Optional<VerificationToken> findByToken(String token);
    void delete(VerificationToken verificationToken);
    VerificationToken save(VerificationToken verificationToken); // Add the save method
    Optional<VerificationToken> findByAppUser(AppUser appUser); // Add method to find by user
}
