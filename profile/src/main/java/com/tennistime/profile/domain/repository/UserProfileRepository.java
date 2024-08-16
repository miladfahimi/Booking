package com.tennistime.profile.domain.repository;

import com.tennistime.profile.domain.model.UserProfile;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository {
    Optional<UserProfile> findById(UUID id);
    UserProfile save(UserProfile userProfile);
    void deleteById(UUID id);
    Optional<UserProfile> findByUserId(UUID userId);
}
