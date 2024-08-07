package com.tennistime.backend.infrastructure.persistence.userDetails;

import com.tennistime.backend.domain.model.userDetails.UserSubscription;
import com.tennistime.backend.domain.repository.userDetails.UserSubscriptionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSubscriptionJpaRepository extends UserSubscriptionRepository, JpaRepository<UserSubscription, Long> {
    @Override
    Optional<UserSubscription> findByUserId(UUID userId);
}
