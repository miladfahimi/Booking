package com.tennistime.authentication.application.controller;

import com.tennistime.authentication.application.service.OtpLoginService;
import com.tennistime.authentication.application.service.OtpService;
import com.tennistime.authentication.application.service.UserService;
import com.tennistime.authentication.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Controller for managing OTP-related operations (Email + SMS).
 * <p>
 * Includes endpoints for sending, validating, and invalidating OTP codes.
 * Existing email-based endpoints remain unchanged. New SMS endpoints have been added.
 */
@RestController
@RequestMapping("/otp")
@Tag(name = "OTP", description = "Operations related to OTP (One Time Password)")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserService userService;

    @Autowired
    private OtpLoginService otpLoginService;

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

    // =====================
    // ======== SMS ========
    // =====================

    /**
     * Send an OTP code to the specified phone number via SMS.
     * <p>
     * The phone number must be in E.164 format (e.g., +46701234567).␊
     * A lightweight user record is provisioned automatically for first-time phone numbers
     * so the subsequent validation step can succeed.
     *
     * @param phone the user's phone number in E.164 format
     * @return HTTP 200 when the OTP dispatch request has been accepted
     */
    @PostMapping("/send-sms")
    @Operation(summary = "Send OTP via SMS to user's phone")
    public ResponseEntity<Void> sendOtpSms(
            @RequestParam
            @NotBlank
            String phone
    ) {
        otpLoginService.sendLoginOtpSms(phone);
        return ResponseEntity.ok().build();
    }


    /**
     * Validate an OTP code sent via SMS and generate a JWT token if valid.
     * <p>
     * The OTP must match the last generated code for the given phone number
     * and must not be expired or invalidated.
     *
     * @param phone the user's phone number in E.164 format
     * @param otp   the OTP code provided by the user
     * @return JWT token as plain text if valid; 401 if invalid; 404 if user not found
     */
    @PostMapping("/validate-sms")
    @Operation(summary = "Validate SMS OTP and return token")
    public ResponseEntity<String> validateOtpSms(
            @RequestParam
            @NotBlank
            String phone,
            @RequestParam
            @NotBlank
            @Pattern(regexp = "^\\d{4,8}$", message = "OTP must be numeric (4-8 digits).")
            String otp
    ) {
        User user = userService.findByPhone(phone);
        if (user != null) {
            String token = otpService.validateOtpAndGenerateTokenSms(user, otp);
            if (token != null) {
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(401).build();
            }
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * Invalidate the current active OTP for the given phone number.
     * <p>
     * This endpoint can be used after a successful login or when a user cancels
     * the verification process.
     *
     * @param phone the user's phone number in E.164 format
     * @return HTTP 200 if the OTP was invalidated; 404 if the user was not found
     */
    @PostMapping("/invalidate-sms")
    @Operation(summary = "Invalidate SMS OTP for a phone")
    public ResponseEntity<Void> invalidateOtpSms(
            @RequestParam
            @NotBlank
            String phone
    ) {
        User user = userService.findByPhone(phone);
        if (user != null) {
            otpService.invalidateOtpSms(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }
}
