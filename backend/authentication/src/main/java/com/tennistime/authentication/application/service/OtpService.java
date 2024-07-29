package com.tennistime.authentication.application.service;

import com.tennistime.authentication.domain.model.AppUser;
import com.tennistime.authentication.domain.repository.AppUserRepository;
import com.tennistime.authentication.infrastructure.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Service class for managing OTP (One Time Password) operations.
 */
@Service
public class OtpService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private static final int OTP_VALID_DURATION = 5; // OTP validity duration in minutes

    /**
     * Generate and send OTP to user's email.
     *
     * @param appUser the user to send OTP to
     */
    @Transactional
    public void generateAndSendOtp(AppUser appUser) {
        String otp = generateOtp();
        appUser.setOtp(otp);
        appUser.setOtpExpirationTime(LocalDateTime.now().plusMinutes(OTP_VALID_DURATION));
        appUserRepository.save(appUser);

        sendOtpEmail(appUser.getEmail(), otp);
    }

    /**
     * Generate a 6-digit OTP.
     *
     * @return the generated OTP
     */
    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
    }

    /**
     * Send OTP to user's email.
     *
     * @param email the email to send OTP to
     * @param otp   the OTP to send
     */
    private void sendOtpEmail(String email, String otp) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Your OTP Code");
            helper.setText("Your OTP code is: " + otp, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    /**
     * Validate the OTP.
     *
     * @param appUser the user to validate OTP for
     * @param otp     the OTP to validate
     * @return true if OTP is valid, false otherwise
     */
    public boolean validateOtp(AppUser appUser, String otp) {
        return appUser.getOtp() != null &&
                appUser.getOtp().equals(otp) &&
                appUser.getOtpExpirationTime().isAfter(LocalDateTime.now());
    }

    /**
     * Invalidate the OTP for the user.
     *
     * @param appUser the user to invalidate OTP for
     */
    @Transactional
    public void invalidateOtp(AppUser appUser) {
        appUser.setOtp(null);
        appUser.setOtpExpirationTime(null);
        appUserRepository.save(appUser);
    }

    /**
     * Validate OTP and generate JWT token if valid.
     *
     * @param appUser the user to validate OTP for
     * @param otp     the OTP to validate
     * @return the generated JWT token if OTP is valid, null otherwise
     */
    @Transactional
    public String validateOtpAndGenerateToken(AppUser appUser, String otp) {
        if (validateOtp(appUser, otp)) {
            invalidateOtp(appUser);
            return jwtUtil.generateToken(appUser.getEmail());
        }
        return null;
    }
}
