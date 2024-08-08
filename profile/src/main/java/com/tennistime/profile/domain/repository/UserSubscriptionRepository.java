package com.tennistime.profile.domain.repository;

import com.tennistime.profile.domain.model.UserSubscription;

import java.util.Optional;
import java.util.UUID;

public interface UserSubscriptionRepository {
    Optional<UserSubscription> findById(Long id);
    UserSubscription save(UserSubscription userSubscription);
    void deleteById(Long id);
    Optional<UserSubscription> findByUserId(UUID userId);
}
