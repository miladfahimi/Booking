package com.tennistime.backend.domain.repository.userDetails;

import com.tennistime.backend.domain.model.userDetails.UserProfile;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository {
    Optional<UserProfile> findById(Long id);
    UserProfile save(UserProfile userProfile);
    void deleteById(Long id);
    Optional<UserProfile> findByUserId(UUID userId);
}
