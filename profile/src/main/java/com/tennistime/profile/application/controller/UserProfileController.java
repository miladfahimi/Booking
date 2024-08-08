package com.tennistime.profile.application.controller;


import com.tennistime.profile.application.dto.UserProfileDTO;
import com.tennistime.profile.application.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user/profiles")
@Tag(name = "User Profile", description = "Manage user profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get user profile", description = "Retrieve a user profile by user ID")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable UUID userId) {
        Optional<UserProfileDTO> userProfile = userProfileService.getUserProfileByUserId(userId);
        return userProfile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create user profile", description = "Create a new user profile")
    public ResponseEntity<UserProfileDTO> createUserProfile(@RequestBody UserProfileDTO userProfileDTO) {
        UserProfileDTO savedUserProfile = userProfileService.createUserProfile(userProfileDTO);
        return ResponseEntity.ok(savedUserProfile);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user profile", description = "Update an existing user profile")
    public ResponseEntity<UserProfileDTO> updateUserProfile(@PathVariable UUID userId, @RequestBody UserProfileDTO updatedProfileDTO) {
        Optional<UserProfileDTO> userProfile = userProfileService.updateUserProfile(userId, updatedProfileDTO);
        return userProfile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user profile", description = "Delete a user profile by user ID")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable UUID userId) {
        userProfileService.deleteUserProfileByUserId(userId);
        return ResponseEntity.ok().build();
    }
}
