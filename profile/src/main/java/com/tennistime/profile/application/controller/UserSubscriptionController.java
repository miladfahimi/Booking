package com.tennistime.profile.application.controller;


import com.tennistime.profile.application.dto.UserSubscriptionDTO;
import com.tennistime.profile.application.service.UserSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user/subscriptions")
@Tag(name = "User Subscription", description = "Manage user subscriptions")
@RequiredArgsConstructor
public class UserSubscriptionController {

    private final UserSubscriptionService userSubscriptionService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get user subscription", description = "Retrieve subscription information for a given user ID")
    public ResponseEntity<UserSubscriptionDTO> getUserSubscription(@PathVariable UUID userId) {
        Optional<UserSubscriptionDTO> userSubscription = userSubscriptionService.getUserSubscriptionByUserId(userId);
        return userSubscription.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create user subscription", description = "Create a new subscription for a user")
    public ResponseEntity<UserSubscriptionDTO> createUserSubscription(@RequestBody UserSubscriptionDTO userSubscriptionDTO) {
        UserSubscriptionDTO savedUserSubscription = userSubscriptionService.createUserSubscription(userSubscriptionDTO);
        return ResponseEntity.ok(savedUserSubscription);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user subscription", description = "Update an existing subscription for a user")
    public ResponseEntity<UserSubscriptionDTO> updateUserSubscription(@PathVariable UUID userId, @RequestBody UserSubscriptionDTO updatedSubscriptionDTO) {
        Optional<UserSubscriptionDTO> userSubscription = userSubscriptionService.updateUserSubscription(userId, updatedSubscriptionDTO);
        return userSubscription.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user subscription", description = "Delete a user subscription")
    public ResponseEntity<Void> deleteUserSubscription(@PathVariable UUID userId) {
        userSubscriptionService.deleteUserSubscriptionByUserId(userId);
        return ResponseEntity.ok().build();
    }
}
