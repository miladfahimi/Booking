package com.tennistime.profile.infrastructure.persistence;

import com.tennistime.profile.domain.model.UserProfile;
import com.tennistime.profile.domain.repository.UserProfileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileJpaRepository extends UserProfileRepository, JpaRepository<UserProfile, UUID> {
    @Override
    Optional<UserProfile> findByUserId(UUID userId);
}
