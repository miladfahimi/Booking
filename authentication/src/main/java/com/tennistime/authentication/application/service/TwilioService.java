package com.tennistime.authentication.application.service;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service class for managing Twilio operations for sending verification SMS.
 */
@Service
public class TwilioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioService.class);

    @Value("${twilio.account.sid:}")
    private String accountSid;

    @Value("${twilio.auth.token:}")
    private String authToken;

    @Value("${twilio.service.sid:}")
    private String serviceSid;

    @Value("123456789")
    private String messagingFromNumber;

    private boolean messagingEnabled;

    @PostConstruct
    public void init() {
        boolean hasCredentials = StringUtils.hasText(accountSid) && StringUtils.hasText(authToken);
        if (hasCredentials) {
            Twilio.init(accountSid, authToken);
            messagingEnabled = StringUtils.hasText(messagingFromNumber);
            if (!messagingEnabled) {
                LOGGER.warn("Twilio messaging from number not configured; SMS OTP delivery disabled");
            }
        } else {
            messagingEnabled = false;
            LOGGER.warn("Twilio credentials not configured; SMS features disabled");
        }
    }

    /**
     * Send verification SMS to a phone number.
     *
     * @param toPhoneNumber the phone number to send verification SMS to
     */
    public void sendVerificationSms(String toPhoneNumber) {
        if (!StringUtils.hasText(serviceSid)) {
            LOGGER.warn("Twilio Verify service SID not configured; skipping verification SMS");
            return;
        }

        Verification verification = Verification.creator(serviceSid, toPhoneNumber, "sms").create();
        LOGGER.debug("Sent verification SMS via Twilio Verify with SID: {}", verification.getSid());
    }

    /**
     * Send a one-time password via SMS using the Messaging API.
     *
     * @param toPhoneNumber destination number in E.164 format
     * @param otp           the OTP code to include in the SMS body
     */
    public void sendOtpSms(String toPhoneNumber, String otp) {
        if (!messagingEnabled) {
            throw new IllegalStateException("Twilio messaging is not configured");
        }

        try {
            Message.creator(
                            new PhoneNumber(toPhoneNumber),
                            new PhoneNumber(messagingFromNumber),
                            "Your OTP code is: " + otp)
                    .create();
        } catch (ApiException apiException) {
            LOGGER.error("Failed to send OTP SMS via Twilio", apiException);
            throw apiException;
        }
    }

    /**
     * Check the verification code sent via SMS.
     *
     * @param toPhoneNumber the phone number to check verification code for
     * @param code          the verification code to check
     * @return true if the verification code is approved, false otherwise
     */
    public boolean checkVerificationCode(String toPhoneNumber, String code) {
        VerificationCheck verificationCheck = VerificationCheck.creator(serviceSid)
                .setTo(toPhoneNumber)
                .setCode(code)
                .create();

        return "approved".equals(verificationCheck.getStatus());
    }

    boolean isMessagingEnabled() {
        return messagingEnabled;
    }
}