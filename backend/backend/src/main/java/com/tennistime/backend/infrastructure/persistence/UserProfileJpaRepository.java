package com.tennistime.backend.infrastructure.persistence;

import com.tennistime.backend.domain.model.UserProfile;
import com.tennistime.backend.domain.repository.UserProfileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileJpaRepository extends UserProfileRepository, JpaRepository<UserProfile, Long> {
    @Override
    Optional<UserProfile> findByUserId(UUID userId); // Ensure this method is implemented
}
