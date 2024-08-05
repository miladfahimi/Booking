package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.UserProfile;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository {
    Optional<UserProfile> findById(Long id);
    UserProfile save(UserProfile userProfile);
    void deleteById(Long id);
    Optional<UserProfile> findByUserId(Long userId); // Ensure this method is present
}
