package com.tennistime.authentication.application.service;

import com.tennistime.authentication.domain.model.AppUser;
import com.tennistime.authentication.domain.model.VerificationToken;
import com.tennistime.authentication.domain.repository.VerificationTokenRepository;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
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

    // Twilio credentials
    public static final String ACCOUNT_SID = "AC10c6673a6eae624392b374b0e6101a7e";
    public static final String AUTH_TOKEN = "dd1cb6eda0694fe8e01e76ec877c2b9a";
    public static final String SERVICE_SID = "VA1b4aaaf73068dcc13fd4b890e8b11dea";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

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

    public void sendVerificationSms(AppUser appUser) {
        Verification.creator(
                SERVICE_SID,
                appUser.getPhone(),
                "sms")
                .create();
    }

    public void regenerateAndSendVerificationSms(AppUser appUser) {
        VerificationToken existingToken = verificationTokenRepository.findByAppUser(appUser)
                .orElseThrow(() -> new IllegalArgumentException("No verification token found for user"));

        // Remove the existing token
        verificationTokenRepository.delete(existingToken);

        // Create and save a new token
        String newToken = UUID.randomUUID().toString();
        VerificationToken newVerificationToken = new VerificationToken(newToken, appUser);
        verificationTokenRepository.save(newVerificationToken);

        sendVerificationSms(appUser);
    }
}
