package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.AuthenticationResponse;
import com.tennistime.backend.application.dto.UserInitializationResponse;
import com.tennistime.backend.application.service.UserInitializationService;
import com.tennistime.backend.domain.model.UserProfile;
import com.tennistime.backend.domain.model.UserSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserInitializationController {

    private final UserInitializationService userInitializationService;

    @PostMapping("/initialize")
    public ResponseEntity<UserInitializationResponse> initializeUser(@RequestBody AuthenticationResponse authResponse) {
        Long userId = authResponse.getId();
        String email = authResponse.getEmail();
        String phone = authResponse.getPhone();

        UserProfile userProfile = userInitializationService.initializeUserProfile(userId, email, phone);
        UserSubscription userSubscription = userInitializationService.initializeUserSubscription(userId);

        UserInitializationResponse response = new UserInitializationResponse(userProfile, userSubscription);
        return ResponseEntity.ok(response);
    }
}
