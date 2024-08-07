package com.tennistime.backend.domain.repository.userDetails;

import com.tennistime.backend.domain.model.userDetails.UserSubscription;

import java.util.Optional;
import java.util.UUID;

public interface UserSubscriptionRepository {
    Optional<UserSubscription> findById(Long id);
    UserSubscription save(UserSubscription userSubscription);
    void deleteById(Long id);
    Optional<UserSubscription> findByUserId(UUID userId);
}
