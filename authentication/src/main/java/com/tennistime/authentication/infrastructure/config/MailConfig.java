package com.tennistime.authentication.infrastructure.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${smtp.server.ip}")
    private String smtpHost;

    @Value("${smtp.server.port}")
    private int smtpPort;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost); // Dynamically set the host
        mailSender.setPort(smtpPort); // Dynamically set the port

        // No need for username and password for FakeSMTP
        // mailSender.setUsername("");
        // mailSender.setPassword("");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.com.tennistime.authentication.authentication", "false");
        props.put("mail.smtp.starttls.enable", "false"); // FakeSMTP does not support TLS
        props.put("mail.debug", "true");

        return mailSender;
    }
}
