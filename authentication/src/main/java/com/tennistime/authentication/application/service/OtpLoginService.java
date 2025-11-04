package com.tennistime.authentication.application.service;

import com.tennistime.authentication.domain.model.User;
import com.tennistime.authentication.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service responsible for handling OTP-based lightweight login flows.
 * <p>
 * This service delegates the core OTP validation and token generation to {@link OtpService}
 * to keep the login-specific orchestration separate from the verification endpoints.
 */
@Service
public class OtpLoginService {

    private final OtpService otpService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OtpLoginService(OtpService otpService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.otpService = otpService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Generate and deliver an OTP code for quick email-based logins.
     * <p>
     * When a user does not yet exist, this helper creates a minimal profile so the
     * follow-up login call can succeed without requiring a full registration flow.
     *
     * @param email the email address that should receive the OTP
     */
    public void sendLoginOtpEmail(String email) {
        User user = userRepository.findByEmail(email).orElseGet(() -> createUserSkeleton(email, null));
        otpService.generateAndSendOtp(user);
    }

    /**
     * Generate and deliver an OTP code for SMS-based quick logins.
     * <p>
     * A lightweight user profile is created on-the-fly for first time visitors so the
     * OTP validation step can issue a JWT token.
     *
     * @param phone the phone number that should receive the OTP via SMS
     */
    public void sendLoginOtpSms(String phone) {
        User user = userRepository.findByPhone(phone).orElseGet(() -> createUserSkeleton(null, phone));
        otpService.generateAndSendOtpSms(user);
    }

    /**
     * Perform an OTP based login for users authenticating with email codes.
     *
     * @param user the user attempting to authenticate
     * @param otp  the OTP provided by the client
     * @return a JWT token when the OTP is valid; {@code null} otherwise
     */
    public String loginWithEmailOtp(User user, String otp) {
        return otpService.validateOtpAndGenerateToken(user, otp);
    }

    /**
     * Perform an OTP based login for users authenticating with SMS codes.
     *
     * @param user the user attempting to authenticate using SMS
     * @param otp  the OTP supplied by the client application
     * @return a JWT token when the OTP is valid; {@code null} otherwise
     */
    public String loginWithSmsOtp(User user, String otp) {
        return otpService.validateOtpAndGenerateTokenSms(user, otp);
    }

    private User createUserSkeleton(String email, String phone) {
        User user = new User();
        user.setEmail(email);
        user.setPhone(phone);
        user.setUsername(email != null ? email : phone);
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEmailVerified(Boolean.FALSE);
        return userRepository.save(user);
    }
}