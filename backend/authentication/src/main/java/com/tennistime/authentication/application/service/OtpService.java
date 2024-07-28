package com.tennistime.authentication.application.service;

import com.tennistime.authentication.domain.model.AppUser;
import com.tennistime.authentication.domain.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AppUserRepository appUserRepository;

    private static final int OTP_VALID_DURATION = 5; // OTP validity duration in minutes

    @Transactional
    public void generateAndSendOtp(AppUser appUser) {
        String otp = generateOtp();
        appUser.setOtp(otp);
        appUser.setOtpExpirationTime(LocalDateTime.now().plusMinutes(OTP_VALID_DURATION));
        appUserRepository.save(appUser);

        sendOtpEmail(appUser.getEmail(), otp);
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
    }

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

    public boolean validateOtp(AppUser appUser, String otp) {
        return appUser.getOtp() != null &&
                appUser.getOtp().equals(otp) &&
                appUser.getOtpExpirationTime().isAfter(LocalDateTime.now());
    }

    @Transactional
    public void invalidateOtp(AppUser appUser) {
        appUser.setOtp(null);
        appUser.setOtpExpirationTime(null);
        appUserRepository.save(appUser);
    }
}
