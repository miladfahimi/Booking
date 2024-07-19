package com.tennistime.backend.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost"); // Set to localhost for FakeSMTP
        mailSender.setPort(2525); // Set to the FakeSMTP port

        // No need for username and password for FakeSMTP
        // mailSender.setUsername("");
        // mailSender.setPassword("");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false"); // FakeSMTP does not support TLS
        props.put("mail.debug", "true");

        return mailSender;
    }
}
