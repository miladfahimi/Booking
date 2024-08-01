package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.UserProfile;

import java.util.Optional;

public interface UserProfileRepository {
    Optional<UserProfile> findByEmail(String email);
}
