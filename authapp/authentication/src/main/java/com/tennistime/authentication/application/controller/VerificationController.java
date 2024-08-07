package com.tennistime.authentication.application.controller;

import com.tennistime.authentication.application.service.UserService;
import com.tennistime.authentication.application.service.VerificationService;
import com.tennistime.authentication.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for user verification operations like verifying email and sending verification SMS.
 */
@RestController
@RequestMapping("/verify")
@Tag(name = "Verification", description = "User verification operations")
public class VerificationController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verificationService;

    @GetMapping("/email")
    @Operation(summary = "Verify user email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        verificationService.verifyEmail(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resend-email")
    @Operation(summary = "Resend verification email")
    public ResponseEntity<Void> resendVerificationEmail(@RequestParam String email) {
        userService.resendVerificationEmail(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-sms")
    @Operation(summary = "Send verification SMS")
    public ResponseEntity<Void> sendVerificationSms(@RequestParam String phone) {
        User user = userService.findByPhone(phone);
        verificationService.sendVerificationSms(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resend-sms")
    @Operation(summary = "Resend verification SMS")
    public ResponseEntity<Void> resendVerificationSms(@RequestParam String phone) {
        User user = userService.findByPhone(phone);
        verificationService.regenerateAndSendVerificationSms(user);
        return ResponseEntity.noContent().build();
    }
}
