package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.service.UserProfileService;
import com.tennistime.backend.domain.model.UserProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user/profiles")
@Tag(name = "User Profile", description = "Manage user profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user profile", description = "Retrieve profile information for a given user ID")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long id) {
        Optional<UserProfile> userProfile = userProfileService.getUserProfile(id);
        return userProfile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create user profile", description = "Create a new profile for a user")
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile) {
        UserProfile savedUserProfile = userProfileService.createUserProfile(userProfile);
        return ResponseEntity.ok(savedUserProfile);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user profile", description = "Update an existing profile for a user")
    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable Long id, @RequestBody UserProfile updatedProfile) {
        Optional<UserProfile> userProfile = userProfileService.updateUserProfile(id, updatedProfile);
        return userProfile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user profile", description = "Delete a user profile")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        userProfileService.deleteUserProfile(id);
        return ResponseEntity.ok().build();
    }
}
