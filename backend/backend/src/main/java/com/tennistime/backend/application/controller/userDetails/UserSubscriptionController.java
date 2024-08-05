package com.tennistime.backend.application.controller.userDetails;

import com.tennistime.backend.application.dto.userDetails.UserSubscriptionDTO;
import com.tennistime.backend.application.service.UserSubscriptionService;
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
    public ResponseEntity<UserSubscriptionDTO> getUserSubscription(@PathVariable Long id) {
        Optional<UserSubscriptionDTO> userSubscription = userSubscriptionService.getUserSubscription(id);
        return userSubscription.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create user subscription", description = "Create a new subscription for a user")
    public ResponseEntity<UserSubscriptionDTO> createUserSubscription(@RequestBody UserSubscriptionDTO userSubscriptionDTO) {
        UserSubscriptionDTO savedUserSubscription = userSubscriptionService.createUserSubscription(userSubscriptionDTO);
        return ResponseEntity.ok(savedUserSubscription);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user subscription", description = "Update an existing subscription for a user")
    public ResponseEntity<UserSubscriptionDTO> updateUserSubscription(@PathVariable Long id, @RequestBody UserSubscriptionDTO updatedSubscriptionDTO) {
        Optional<UserSubscriptionDTO> userSubscription = userSubscriptionService.updateUserSubscription(id, updatedSubscriptionDTO);
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
