package com.tennistime.authentication.application.controller;

import com.tennistime.authentication.application.dto.AppUserDTO;
import com.tennistime.authentication.application.service.OtpService;
import com.tennistime.authentication.application.service.UserService;
import com.tennistime.authentication.domain.model.AppUser;
import com.tennistime.authentication.infrastructure.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication")
@Tag(name = "Authentication", description = "User authentication operations")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/send-otp")
    @Operation(summary = "Send OTP to user email")
    public ResponseEntity<Void> sendOtp(@RequestParam String email) {
        AppUserDTO userDTO = userService.findByEmail(email);
        if (userDTO != null) {
            AppUser appUser = userService.findEntityByEmail(email);
            otpService.generateAndSendOtp(appUser);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP and sign in")
    public ResponseEntity<AppUserDTO> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        AppUser appUser = userService.findEntityByEmail(email);
        if (appUser != null && otpService.validateOtp(appUser, otp)) {
            otpService.invalidateOtp(appUser);
            String token = jwtUtil.generateToken(appUser.getEmail());
            AppUserDTO userDTO = userService.findByEmail(email);
            userDTO.setToken(token);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}
