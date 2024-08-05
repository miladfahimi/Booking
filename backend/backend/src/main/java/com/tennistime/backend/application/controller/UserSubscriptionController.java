package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.service.UserSubscriptionService;
import com.tennistime.backend.domain.model.UserSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user/subscriptions")
@RequiredArgsConstructor
public class UserSubscriptionController {

    private final UserSubscriptionService userSubscriptionService;

    @GetMapping("/{id}")
    public ResponseEntity<UserSubscription> getUserSubscription(@PathVariable Long id) {
        Optional<UserSubscription> userSubscription = userSubscriptionService.getUserSubscription(id);
        return userSubscription.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserSubscription> createUserSubscription(@RequestBody UserSubscription userSubscription) {
        UserSubscription savedUserSubscription = userSubscriptionService.createUserSubscription(userSubscription);
        return ResponseEntity.ok(savedUserSubscription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSubscription> updateUserSubscription(@PathVariable Long id, @RequestBody UserSubscription updatedSubscription) {
        Optional<UserSubscription> userSubscription = userSubscriptionService.updateUserSubscription(id, updatedSubscription);
        return userSubscription.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSubscription(@PathVariable Long id) {
        userSubscriptionService.deleteUserSubscription(id);
        return ResponseEntity.ok().build();
    }
}
