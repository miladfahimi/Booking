package com.tennistime.authentication.application.controller;

import com.tennistime.authentication.application.service.OtpLoginService;
import com.tennistime.authentication.application.service.UserService;
import com.tennistime.authentication.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposing lightweight OTP login endpoints.
 */
@RestController
@RequestMapping("/otp")
@Tag(name = "OTP Login", description = "Endpoints dedicated to OTP-based quick login")
public class OtpLoginController {

    private final OtpLoginService otpLoginService;
    private final UserService userService;

    @Autowired
    public OtpLoginController(OtpLoginService otpLoginService, UserService userService) {
        this.otpLoginService = otpLoginService;
        this.userService = userService;
    }

    /**
     * Send an OTP code to the provided email so the client can complete a quick login flow.
     * <p>
     * The endpoint is designed for both existing and brand-new users: when no profile
     * is found, a minimal placeholder user is created to keep the flow seamless.
     *
     * @param email the email address that should receive the login OTP
     * @return HTTP 200 after the OTP has been dispatched
     */
    @PostMapping("/login/send")
    @Operation(summary = "Send login OTP to email")
    public ResponseEntity<Void> sendOtpForEmailLogin(@RequestParam String email) {
        otpLoginService.sendLoginOtpEmail(email);
        return ResponseEntity.ok().build();
    }

    /**
     * Send an OTP code via SMS for a lightweight login experience.
     * <p>
     * If the phone number has not been seen before, the backing service will create
     * a minimal user record so the follow-up validation step succeeds without a
     * separate registration process.
     *
     * @param phone the phone number that should receive the login OTP
     * @return HTTP 200 after the OTP has been dispatched
     */
    @PostMapping("/login/send-sms")
    @Operation(summary = "Send login OTP to phone via SMS")
    public ResponseEntity<Void> sendOtpForSmsLogin(
            @RequestParam
            @NotBlank
            String phone
    ) {
        otpLoginService.sendLoginOtpSms(phone);
        return ResponseEntity.ok().build();
    }

    /**
     * Perform a lightweight login using email-delivered OTP codes.
     *
     * @param email the user's email address
     * @param otp   the OTP submitted by the user
     * @return JWT token when the OTP is valid; 401 for invalid OTP; 404 when user is missing
     */
    @PostMapping("/login")
    @Operation(summary = "Login using email OTP")
    public ResponseEntity<String> loginWithOtp(@RequestParam String email, @RequestParam String otp) {
        User user = userService.findEntityByEmail(email);
        if (user != null) {
            String token = otpLoginService.loginWithEmailOtp(user, otp);
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
     * Perform an SMS-based OTP login, returning a JWT token when successful.
     *
     * @param phone the user's phone number in E.164 format
     * @param otp   the OTP submitted by the user
     * @return JWT token when the OTP is valid; 401 for invalid OTP; 404 when user is missing
     */
    @PostMapping("/login-sms")
    @Operation(summary = "Login using SMS OTP")
    public ResponseEntity<String> loginWithOtpSms(
            @RequestParam
            @NotBlank
            String phone,
            @RequestParam
            @NotBlank
            @Pattern(regexp = "^\\d{4,8}$", message = "OTP must be numeric (4-8 digits).")
            String otp
    ) {
        User user = userService.findEntityByPhone(phone);
        if (user != null) {
            String token = otpLoginService.loginWithSmsOtp(user, otp);
            if (token != null) {
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(401).build();
            }
        } else {
            return ResponseEntity.status(404).build();
        }
    }
}
