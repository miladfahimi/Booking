package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.AppUser;
import com.tennistime.backend.domain.model.VerificationToken;
import com.tennistime.backend.domain.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.UUID;

@Service
public class VerificationService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Transactional
    public void sendVerificationEmail(AppUser appUser) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, appUser);
        verificationTokenRepository.save(verificationToken);

        String verificationLink = "http://localhost:8080/api/v1/users/verify?token=" + token;

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(appUser.getEmail());
            helper.setSubject("Email Verification");
            helper.setText("Please click the following link to verify your email: " + verificationLink, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Transactional
    public void verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));
        AppUser appUser = verificationToken.getAppUser();
        appUser.setRole("VERIFIED_USER");
        verificationTokenRepository.deleteByToken(token);
    }
}
