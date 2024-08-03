package com.tennistime.authentication.application.controller;

import com.tennistime.authentication.application.dto.UserDTO;
import com.tennistime.authentication.application.service.UserService;
import com.tennistime.authentication.infrastructure.security.JwtUtil;
import com.tennistime.authentication.redis.TokenBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controller for user authentication operations like signup, signin, and logout.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User authentication operations")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    @Operation(summary = "Sign up a new user")
    public ResponseEntity<?> signup(@Valid @RequestBody UserDTO userDTO) {
        try {
            UserDTO signedUpUser = userService.signup(userDTO);

            // Logging
            System.out.println("\033[1;35m----------------------------\033[0m");
            System.out.println("\033[1;35m[AuthenticationController] Signup response: " + signedUpUser + "\033[0m");
            System.out.println("\033[1;35m----------------------------\033[0m");

            return ResponseEntity.ok(signedUpUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/signin")
    @Operation(summary = "Sign in a user")
    public ResponseEntity<UserDTO> signin(@RequestParam String email, @RequestParam String password) {
        UserDTO signedInUser = userService.signin(email, password);

        // Logging
        System.out.println("\033[1;32m----------------------------\033[0m");
        System.out.println("\033[1;32m[AuthenticationController] Signin response: " + signedInUser + "\033[0m");
        System.out.println("\033[1;32m----------------------------\033[0m");

        return ResponseEntity.ok(signedInUser);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout a user")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            long expirationTime = jwtUtil.getExpirationDateFromToken(jwt).getTime() - System.currentTimeMillis();
            tokenBlacklistService.blacklistToken(jwt, expirationTime);

            userService.logout(jwt);

            // Logging
            System.out.println("\033[1;31m----------------------------\033[0m");
            System.out.println("\033[1;31m[AuthenticationController] Token blacklisted: " + jwt + "\033[0m");
            System.out.println("\033[1;31m----------------------------\033[0m");
        }
        return ResponseEntity.noContent().build();
    }
}
