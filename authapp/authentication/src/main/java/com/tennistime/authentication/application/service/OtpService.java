package com.tennistime.authentication.application.service;

import com.tennistime.authentication.domain.model.User;
import com.tennistime.authentication.domain.repository.UserRepository;
import com.tennistime.authentication.infrastructure.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * Service class for managing OTP (One Time Password) operations.
 */
@Service
public class OtpService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final int OTP_VALID_DURATION = 5; // OTP validity duration in minutes

    /**
     * Generate and send OTP to user's email.
     *
     * @param user the user to send OTP to
     */
    @Transactional
    public void generateAndSendOtp(User user) {
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpirationTime(LocalDateTime.now().plusMinutes(OTP_VALID_DURATION));
        userRepository.save(user);

        sendOtpEmail(user.getEmail(), otp);
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
     * @param user the user to validate OTP for
     * @param otp  the OTP to validate
     * @return true if OTP is valid, false otherwise
     */
    public boolean validateOtp(User user, String otp) {
        return user.getOtp() != null &&
                user.getOtp().equals(otp) &&
                user.getOtpExpirationTime().isAfter(LocalDateTime.now());
    }

    /**
     * Invalidate the OTP for the user.
     *
     * @param user the user to invalidate OTP for
     */
    @Transactional
    public void invalidateOtp(User user) {
        user.setOtp(null);
        user.setOtpExpirationTime(null);
        userRepository.save(user);
    }

    /**
     * Validate OTP and generate JWT token if valid.
     *
     * @param user the user to validate OTP for
     * @param otp  the OTP to validate
     * @return the generated JWT token if OTP is valid, null otherwise
     */
    @Transactional
    public String validateOtpAndGenerateToken(User user, String otp) {
        if (validateOtp(user, otp)) {
            invalidateOtp(user);
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            return jwtUtil.generateToken(userDetails);
        }
        return null;
    }
}
