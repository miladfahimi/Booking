package com.tennistime.authentication;

import com.tennistime.authentication.application.service.VerificationInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = {"com.tennistime.authentication", "com.tennistime.common"})
public class TennistimeAuthenticationApplication implements CommandLineRunner {

    @Autowired
    private VerificationInitializationService verificationInitializationService;

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(TennistimeAuthenticationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\033[1;31m----------------------------\033[0m");
        System.out.println("\033[1;31mActive Profiles Authentication: " + env.getActiveProfiles() + "\033[0m");
        System.out.println("\033[1;31m----------------------------\033[0m");
        verificationInitializationService.initializeVerificationTokens();
    }
}
