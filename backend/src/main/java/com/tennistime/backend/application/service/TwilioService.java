package com.tennistime.backend.application.service;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.service.sid}")
    private String serviceSid;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendVerificationSms(String toPhoneNumber) {
        Verification verification = Verification.creator(serviceSid, toPhoneNumber, "sms").create();
        System.out.println("Verification SID: " + verification.getSid());
    }

    public boolean checkVerificationCode(String toPhoneNumber, String code) {
        VerificationCheck verificationCheck = VerificationCheck.creator(serviceSid)
                .setTo(toPhoneNumber)
                .setCode(code)
                .create();

        return "approved".equals(verificationCheck.getStatus());
    }
}
