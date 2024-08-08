package com.tennistime.profile.application.controller;

import com.tennistime.profile.application.dto.UserInitializationRequestDTO;
import com.tennistime.profile.application.dto.UserInitializationResponseDTO;
import com.tennistime.profile.application.service.UserInitializationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Initialization", description = "User Initialization Operations")
public class UserInitializationController {

    private final UserInitializationService userInitializationService;

    @PostMapping("/initialize")
    @Operation(summary = "Initialize User Entities")
    public ResponseEntity<UserInitializationResponseDTO> initializeUser(
            @RequestBody UserInitializationRequestDTO request) {
        try {
            UserInitializationResponseDTO response = userInitializationService.initializeUserEntities(request);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(null); // Conflict status code
        }
    }

    @GetMapping("/initialize/{userId}")
    @Operation(summary = "Get User Initialization by User ID", description = "Retrieve initialization data for a given user ID.")
    public ResponseEntity<UserInitializationResponseDTO> getUserInitialization(@PathVariable UUID userId) {
        Optional<UserInitializationResponseDTO> response = userInitializationService.getUserInitializationByUserId(userId);
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
