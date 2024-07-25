package com.tennistime.authentication.application.controller;

import com.tennistime.authentication.application.dto.AppUserDTO;
import com.tennistime.authentication.application.service.UserService;
import com.tennistime.authentication.application.service.VerificationService;
import com.tennistime.authentication.domain.model.AppUser;
import com.tennistime.authentication.infrastructure.security.JwtUtil;
import com.tennistime.authentication.infrastructure.security.TokenBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations related to user management")
public class AppUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<List<AppUserDTO>> getAllUsers() {
        List<AppUserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<AppUserDTO> getUserById(@PathVariable Long id) {
        AppUserDTO user = userService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<AppUserDTO> createUser(@Valid @RequestBody AppUserDTO appUserDTO) {
        AppUserDTO createdUser = userService.save(appUserDTO);
        return ResponseEntity.ok(createdUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signup")
    @Operation(summary = "Sign up a new user")
    public ResponseEntity<AppUserDTO> signup(@Valid @RequestBody AppUserDTO appUserDTO) {
        AppUserDTO signedUpUser = userService.signup(appUserDTO);

        // Logging
        System.out.println("\033[1;32m----------------------------\033[0m");
        System.out.println("\033[1;32m[AppUserController] Signup response: " + signedUpUser + "\033[0m");
        System.out.println("\033[1;32m----------------------------\033[0m");

        return ResponseEntity.ok(signedUpUser);
    }

    @PostMapping("/signin")
    @Operation(summary = "Sign in a user")
    public ResponseEntity<AppUserDTO> signin(@RequestParam String email, @RequestParam String password) {
        AppUserDTO signedInUser = userService.signin(email, password);

        // Logging
        System.out.println("\033[1;32m----------------------------\033[0m");
        System.out.println("\033[1;32m[AppUserController] Signin response: " + signedInUser + "\033[0m");
        System.out.println("\033[1;32m----------------------------\033[0m");

        return ResponseEntity.ok(signedInUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            tokenBlacklistService.blacklistToken(jwt);

            // Logging
            System.out.println("\033[1;31m----------------------------\033[0m");
            System.out.println("\033[1;31m[AppUserController] Token blacklisted: " + jwt + "\033[0m");
            System.out.println("\033[1;31m----------------------------\033[0m");
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify")
    @Operation(summary = "Verify user email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        verificationService.verifyEmail(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resend-verification")
    @Operation(summary = "Resend verification email")
    public ResponseEntity<Void> resendVerificationEmail(@RequestParam String email) {
        userService.resendVerificationEmail(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-verification-sms")
    @Operation(summary = "Send verification SMS")
    public ResponseEntity<Void> sendVerificationSms(@RequestParam String phone) {
        AppUser appUser = userService.findByPhone(phone);
        verificationService.sendVerificationSms(appUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resend-verification-sms")
    @Operation(summary = "Resend verification SMS")
    public ResponseEntity<Void> resendVerificationSms(@RequestParam String phone) {
        AppUser appUser = userService.findByPhone(phone);
        verificationService.regenerateAndSendVerificationSms(appUser);
        return ResponseEntity.noContent().build();
    }


}
