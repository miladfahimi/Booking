package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.UserSubscription;
import com.tennistime.backend.domain.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;

    public Optional<UserSubscription> getUserSubscription(Long id) {
        return userSubscriptionRepository.findById(id);
    }

    public UserSubscription createUserSubscription(UserSubscription userSubscription) {
        return userSubscriptionRepository.save(userSubscription);
    }

    public Optional<UserSubscription> updateUserSubscription(Long id, UserSubscription updatedSubscription) {
        return userSubscriptionRepository.findById(id).map(userSubscription -> {
            userSubscription.setSubscriptionPlan(updatedSubscription.getSubscriptionPlan());
            userSubscription.setStartDate(updatedSubscription.getStartDate());
            userSubscription.setEndDate(updatedSubscription.getEndDate());
            userSubscription.setStatus(updatedSubscription.getStatus());
            return userSubscriptionRepository.save(userSubscription);
        });
    }

    public void deleteUserSubscription(Long id) {
        userSubscriptionRepository.deleteById(id);
    }
}
