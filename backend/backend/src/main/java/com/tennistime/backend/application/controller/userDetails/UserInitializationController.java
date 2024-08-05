package com.tennistime.backend.application.controller.userDetails;

import com.tennistime.backend.application.dto.AuthenticationResponse;
import com.tennistime.backend.application.dto.userDetails.UserInitializationResponse;
import com.tennistime.backend.application.service.UserInitializationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User Initialization", description = "Initialize user profile and subscription")
@RequiredArgsConstructor
public class UserInitializationController {

    private final UserInitializationService userInitializationService;

    @PostMapping("/initialize")
    @Operation(summary = "Initialize user", description = "Initialize user profile and subscription based on authentication response")
    public ResponseEntity<UserInitializationResponse> initializeUser(@RequestBody AuthenticationResponse authResponse) {
        Long userId = authResponse.getId();
        String email = authResponse.getEmail();
        String phone = authResponse.getPhone();

        UserInitializationResponse response = userInitializationService.initializeUserEntities(userId, email, phone);
        return ResponseEntity.ok(response);
    }
}
