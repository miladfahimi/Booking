package com.tennistime.authentication.domain.repository;


import com.tennistime.authentication.domain.model.User;
import com.tennistime.authentication.domain.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository {
    Optional<VerificationToken> findByToken(String token);
    void delete(VerificationToken verificationToken);
    VerificationToken save(VerificationToken verificationToken); // Add the save method
    Optional<VerificationToken> findByUser(User user); // Add method to find by user
}
