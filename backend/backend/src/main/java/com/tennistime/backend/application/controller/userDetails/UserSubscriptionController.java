package com.tennistime.backend.application.controller.userDetails;

import com.tennistime.backend.application.service.UserSubscriptionService;
import com.tennistime.backend.domain.model.UserSubscription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user/subscriptions")
@Tag(name = "User Subscription", description = "Manage user subscriptions")
@RequiredArgsConstructor
public class UserSubscriptionController {

    private final UserSubscriptionService userSubscriptionService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user subscription", description = "Retrieve subscription information for a given user ID")
    public ResponseEntity<UserSubscription> getUserSubscription(@PathVariable Long id) {
        Optional<UserSubscription> userSubscription = userSubscriptionService.getUserSubscription(id);
        return userSubscription.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create user subscription", description = "Create a new subscription for a user")
    public ResponseEntity<UserSubscription> createUserSubscription(@RequestBody UserSubscription userSubscription) {
        UserSubscription savedUserSubscription = userSubscriptionService.createUserSubscription(userSubscription);
        return ResponseEntity.ok(savedUserSubscription);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user subscription", description = "Update an existing subscription for a user")
    public ResponseEntity<UserSubscription> updateUserSubscription(@PathVariable Long id, @RequestBody UserSubscription updatedSubscription) {
        Optional<UserSubscription> userSubscription = userSubscriptionService.updateUserSubscription(id, updatedSubscription);
        return userSubscription.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user subscription", description = "Delete a user subscription")
    public ResponseEntity<Void> deleteUserSubscription(@PathVariable Long id) {
        userSubscriptionService.deleteUserSubscription(id);
        return ResponseEntity.ok().build();
    }
}
