package com.tennistime.authentication.application.service;

import com.tennistime.authentication.domain.model.User;
import com.tennistime.authentication.domain.model.VerificationToken;
import com.tennistime.authentication.domain.repository.VerificationTokenRepository;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service class for managing verification operations like sending verification emails and SMS.
 */
@Service
public class VerificationService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TwilioService twilioService;

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.service.sid}")
    private String serviceSid;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

    /**
     * Send a verification email to the user.
     *
     * @param user the user to send the email to
     */
    @Transactional
    public void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);

        String verificationLink = "http://localhost:8080/api/v1/users/verify?token=" + token;
        String htmlLink = "<a href=\"" + verificationLink + "\">Click here to verify your account</a>";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Email Verification");
            helper.setText("Please click the following link to verify your email: " + htmlLink, true);
            javaMailSender.send(message);

            // Logging
            System.out.println("\033[1;32m----------------------------\033[0m");
            System.out.println("\033[1;32m[VerificationService] Verification email sent to: " + user.getEmail() + "\033[0m");
            System.out.println("\033[1;32m----------------------------\033[0m");
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Verify the user's email using the provided token.
     *
     * @param token the verification token
     */
    @Transactional
    public void verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));
        User user = verificationToken.getUser();
        String role = user.getRole();
        user.setRole("VERIFIED_" + role);
        verificationTokenRepository.delete(verificationToken);

        // Logging
        System.out.println("\033[1;32m----------------------------\033[0m");
        System.out.println("\033[1;32m[VerificationService] Verified user: " + user.getEmail() + " as VERIFIED_" + role + "\033[0m");
        System.out.println("\033[1;32m----------------------------\033[0m");
    }

    /**
     * Regenerate and send a new verification token to the user.
     *
     * @param user the user to send the token to
     */
    @Transactional
    public void regenerateAndSendVerificationToken(User user) {
        VerificationToken existingToken = verificationTokenRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("No verification token found for user"));

        // Remove the existing token
        verificationTokenRepository.delete(existingToken);

        // Create and save a new token
        String newToken = UUID.randomUUID().toString();
        VerificationToken newVerificationToken = new VerificationToken(newToken, user);
        verificationTokenRepository.save(newVerificationToken);
        String verificationLink = "http://localhost:8095/api/v1/verify/email?token=" + newToken;
        String htmlLink = "<a href=\"" + verificationLink + "\">Click here to verify your account</a>";

        // Logic to send email with the new verification link
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Email Verification");
            helper.setText("Please click the following link to verify your email: " + htmlLink, true);
            javaMailSender.send(message);

            // Logging
            System.out.println("\033[1;32m----------------------------\033[0m");
            System.out.println("\033[1;32m[VerificationService] Resent verification email to: " + user.getEmail() + "\033[0m");
            System.out.println("\033[1;32m----------------------------\033[0m");
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Send a verification SMS to the user.
     *
     * @param user the user to send the SMS to
     */
    public void sendVerificationSms(User user) {
        Verification.creator(
                        serviceSid,
                        user.getPhone(),
                        "sms")
                .create();

        // Logging
        System.out.println("\033[1;32m----------------------------\033[0m");
        System.out.println("\033[1;32m[VerificationService] Verification SMS sent to: " + user.getPhone() + "\033[0m");
        System.out.println("\033[1;32m----------------------------\033[0m");
    }

    /**
     * Regenerate and send a new verification SMS to the user.
     *
     * @param user the user to send the SMS to
     */
    public void regenerateAndSendVerificationSms(User user) {
        VerificationToken existingToken = verificationTokenRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("No verification token found for user"));

        // Remove the existing token
        verificationTokenRepository.delete(existingToken);

        // Create and save a new token
        String newToken = UUID.randomUUID().toString();
        VerificationToken newVerificationToken = new VerificationToken(newToken, user);
        verificationTokenRepository.save(newVerificationToken);

        sendVerificationSms(user);
    }
}
