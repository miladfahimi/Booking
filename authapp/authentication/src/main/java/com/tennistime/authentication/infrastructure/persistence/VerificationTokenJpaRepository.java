package com.tennistime.authentication.infrastructure.persistence;

import com.tennistime.authentication.domain.model.User;
import com.tennistime.authentication.domain.model.VerificationToken;
import com.tennistime.authentication.domain.repository.VerificationTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenJpaRepository extends VerificationTokenRepository, JpaRepository<VerificationToken, UUID> {  // Changed from Long to UUID
    Optional<VerificationToken> findByToken(String token);
    void delete(VerificationToken verificationToken);
    Optional<VerificationToken> findByUser(User user); // Add method to find by user
}
