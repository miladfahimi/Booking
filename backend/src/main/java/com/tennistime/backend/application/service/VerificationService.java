package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.AppUser;
import com.tennistime.backend.domain.model.VerificationToken;
import com.tennistime.backend.domain.repository.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        String htmlLink = "<a href=\"" + verificationLink + "\">Click here to verify your account</a>";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(appUser.getEmail());
            helper.setSubject("Email Verification");
            helper.setText("Please click the following link to verify your email: " + htmlLink, true);
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
        String role = appUser.getRole();
        appUser.setRole("VERIFIED_" + role);
        // Logging
        System.out.println("\033[1;32m----------------------------\033[0m");
        System.out.println("\033[1;32m[VerificationService] Verified user as: " + "VERIFIED_" + role + "\033[0m");
        System.out.println("\033[1;32m----------------------------\033[0m");
        verificationTokenRepository.delete(verificationToken);
    }

    public void regenerateAndSendVerificationToken(AppUser appUser) {
        VerificationToken existingToken = verificationTokenRepository.findByAppUser(appUser)
                .orElseThrow(() -> new IllegalArgumentException("No verification token found for user"));

        // Remove the existing token
        verificationTokenRepository.delete(existingToken);

        // Create and save a new token
        String newToken = UUID.randomUUID().toString();
        VerificationToken newVerificationToken = new VerificationToken(newToken, appUser);
        verificationTokenRepository.save(newVerificationToken);
        String verificationLink = "http://localhost:8080/api/v1/users/verify?token=" + newToken;
        String htmlLink = "<a href=\"" + verificationLink + "\">Click here to verify your account</a>";


        // Logic to send email with the new verification link
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(appUser.getEmail());
            helper.setSubject("Email Verification");
            helper.setText("Please click the following link to verify your email: " + htmlLink, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
