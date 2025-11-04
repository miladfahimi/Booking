package com.tennistime.authentication.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Helper responsible for delivering OTP codes to phone numbers.
 * <p>
 * Uses Twilio when messaging credentials are available. Otherwise it simply logs the OTP so
 * developers can complete the flow while a real SMS gateway is unavailable.
 */
@Component
class PhoneOtpHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneOtpHelper.class);

    private final TwilioService twilioService;

    PhoneOtpHelper(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    /**
     * Attempt to send an OTP via SMS.
     *
     * @param phoneNumber destination phone number in E.164 format
     * @param otp         code to deliver
     * @return {@code true} when the message was handed off to Twilio, {@code false} otherwise
     */
    boolean sendOtp(String phoneNumber, String otp) {
        if (!StringUtils.hasText(phoneNumber)) {
            LOGGER.warn("Attempted to send OTP to empty phone number");
            return false;
        }

        if (twilioService.isMessagingEnabled()) {
            try {
                twilioService.sendOtpSms(phoneNumber, otp);
                return true;
            } catch (RuntimeException runtimeException) {
                LOGGER.warn("Twilio SMS delivery failed for {}", phoneNumber, runtimeException);
            }
        }

        LOGGER.info("SMS gateway unavailable. OTP for {} is {}", phoneNumber, otp);
        return false;
    }
}