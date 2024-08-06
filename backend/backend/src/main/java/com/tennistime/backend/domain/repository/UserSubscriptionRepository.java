package com.tennistime.backend.domain.repository;

import com.tennistime.backend.domain.model.UserSubscription;

import java.util.Optional;

public interface UserSubscriptionRepository {
    Optional<UserSubscription> findById(Long id);
    UserSubscription save(UserSubscription userSubscription);
    void deleteById(Long id);
    Optional<UserSubscription> findByUserProfileId(Long userProfileId);
}
