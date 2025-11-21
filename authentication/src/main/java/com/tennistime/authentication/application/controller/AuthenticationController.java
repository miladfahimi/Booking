package com.tennistime.authentication.application.controller;

import com.tennistime.authentication.application.dto.RefreshTokenRequest;
import com.tennistime.authentication.application.dto.SignInRequest;
import com.tennistime.authentication.application.dto.TokenResponse;
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

import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> signup(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
        try {

            String ip = request.getRemoteAddr();
            String deviceModel = userDTO.getDeviceModel();
            String os = userDTO.getOs();
            String browser = userDTO.getBrowser();

            userDTO = userService.signup(userDTO, ip, deviceModel, os, browser);

            // Logging the new data
            System.out.println("\033[1;32m----------------------------\033[0m");
            System.out.println("\033[1;32m[AuthenticationController] Signup new data: IP=" + ip + ", DeviceModel=" + deviceModel + ", OS=" + os + ", Browser=" + browser + "\033[0m");
            System.out.println("\033[1;32m----------------------------\033[0m");

            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/signin")
    @Operation(summary = "Sign in a user")
    public ResponseEntity<UserDTO> signin(@RequestBody SignInRequest signInRequest, HttpServletRequest request) {
        // Logging the new data
        System.out.println("\033[1;32m----------------------------\033[0m");
        System.out.println("Received SignInRequest: " + signInRequest);
        System.out.println("\033[1;32m----------------------------\033[0m");
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();
        String ip = request.getRemoteAddr();
        String deviceModel = signInRequest.getDeviceModel();
        String os = signInRequest.getOs();
        String browser = signInRequest.getBrowser();
        boolean rememberMe = signInRequest.isRememberMe();

        UserDTO signedInUser = userService.signin(email, password, ip, deviceModel, os, browser, rememberMe);

        // Logging the new data
        System.out.println("\033[1;32m----------------------------\033[0m");
        System.out.println("\033[1;32m[AuthenticationController] Signin new data: IP=" + ip + ", DeviceModel=" + deviceModel + ", OS=" + os + ", Browser=" + browser + "\033[0m");
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

    /**
     * Exchanges a valid refresh token for a new access token and rotated refresh token.
     *
     * @param request the request containing the refresh token
     * @return the refreshed token pair
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        TokenResponse tokenResponse = userService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(tokenResponse);
    }

    // Utility methods to extract device, OS, and browser from the User-Agent string
    private String extractDeviceModel(String userAgent) {
        // Implement logic or use a library to parse the User-Agent string
        return "unknown device";
    }

    private String extractOperatingSystem(String userAgent) {
        // Implement logic or use a library to parse the User-Agent string
        return "unknown OS";
    }

    private String extractBrowser(String userAgent) {
        // Implement logic or use a library to parse the User-Agent string
        return "unknown browser";
    }
}
