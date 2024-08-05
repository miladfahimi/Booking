package com.tennistime.backend.application.controller.userDetails;

import com.tennistime.backend.application.dto.userDetails.UserProfileDTO;
import com.tennistime.backend.application.service.UserProfileService;
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
    @Operation(summary = "Get user profile", description = "Retrieve a user profile by ID")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long id) {
        Optional<UserProfileDTO> userProfile = userProfileService.getUserProfile(id);
        return userProfile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create user profile", description = "Create a new user profile")
    public ResponseEntity<UserProfileDTO> createUserProfile(@RequestBody UserProfileDTO userProfileDTO) {
        UserProfileDTO savedUserProfile = userProfileService.createUserProfile(userProfileDTO);
        return ResponseEntity.ok(savedUserProfile);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user profile", description = "Update an existing user profile")
    public ResponseEntity<UserProfileDTO> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDTO updatedProfileDTO) {
        Optional<UserProfileDTO> userProfile = userProfileService.updateUserProfile(id, updatedProfileDTO);
        return userProfile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user profile", description = "Delete a user profile by ID")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        userProfileService.deleteUserProfile(id);
        return ResponseEntity.ok().build();
    }
}
