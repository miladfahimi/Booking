package com.tennistime.authentication.application.service;

import com.tennistime.authentication.domain.model.AppUser;
import com.tennistime.authentication.domain.model.VerificationToken;
import com.tennistime.authentication.domain.repository.AppUserRepository;
import com.tennistime.authentication.domain.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for initializing verification tokens for existing users who don't have them.
 */
@Service
public class VerificationInitializationService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Transactional
    public void initializeVerificationTokens() {
        // Fetch all users who don't have a verification token
        Iterable<AppUser> users = appUserRepository.findAll();
        for (AppUser user : users) {
            if (!verificationTokenRepository.findByAppUser(user).isPresent()) {
                // Create a new verification token for the user
                String token = UUID.randomUUID().toString();
                VerificationToken verificationToken = new VerificationToken(token, user);
                verificationTokenRepository.save(verificationToken);

                // Logging
                System.out.println("\033[1;32m----------------------------\033[0m");
                System.out.println("\033[1;32m[VerificationInitializationService] Token created for user: " + user.getEmail() + "\033[0m");
                System.out.println("\033[1;32m----------------------------\033[0m");
            }
        }
    }
}
