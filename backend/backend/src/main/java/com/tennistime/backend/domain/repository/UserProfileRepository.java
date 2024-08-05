package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.UserProfile;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository {
    Optional<UserProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    UserProfile save(UserProfile userProfile);
    Optional<UserProfile> findByEmail(String email);
}
