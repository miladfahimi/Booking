package com.tennistime.authentication.application.controller;

import com.tennistime.authentication.application.service.OtpService;
import com.tennistime.authentication.application.service.UserService;
import com.tennistime.authentication.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing OTP-related operations.
 */
@RestController
@RequestMapping("/otp")
@Tag(name = "OTP", description = "Operations related to OTP (One Time Password)")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserService userService;

    @PostMapping("/send")
    @Operation(summary = "Send OTP to user's email")
    public ResponseEntity<Void> sendOtp(@RequestParam String email) {
        User user = userService.findEntityByEmail(email);
        if (user != null) {
            otpService.generateAndSendOtp(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate OTP")
    public ResponseEntity<String> validateOtp(@RequestParam String email, @RequestParam String otp) {
        User user = userService.findEntityByEmail(email);
        if (user != null) {
            String token = otpService.validateOtpAndGenerateToken(user, otp);
            if (token != null) {
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(401).build();
            }
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/invalidate")
    @Operation(summary = "Invalidate OTP")
    public ResponseEntity<Void> invalidateOtp(@RequestParam String email) {
        User user = userService.findEntityByEmail(email);
        if (user != null) {
            otpService.invalidateOtp(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }
}
