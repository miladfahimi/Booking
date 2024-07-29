package com.tennistime.authentication.application.service;

import com.tennistime.authentication.application.dto.AppUserDTO;
import com.tennistime.authentication.application.mapper.AppUserMapper;
import com.tennistime.authentication.domain.model.AppUser;
import com.tennistime.authentication.domain.repository.AppUserRepository;
import com.tennistime.authentication.infrastructure.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

/**
 * Service class for managing user-related operations for clients.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Environment env;

    @Autowired
    private OtpService otpService;

    /**
     * Sign up a new user.
     *
     * @param appUserDTO the user to sign up
     * @return the signed-up user
     */
    public AppUserDTO signup(AppUserDTO appUserDTO) {
        if (appUserRepository.findByEmail(appUserDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (appUserDTO.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        // Save the raw password for later login
        String rawPassword = appUserDTO.getPassword();

        appUserDTO.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        appUser = appUserRepository.save(appUser);
        verificationService.sendVerificationEmail(appUser);

        // Logging
        System.out.println("\033[1;32m----------------------------\033[0m");
        System.out.println("\033[1;32m[UserService] Signup successful for user: " + appUser.getEmail() + "\033[0m");
        System.out.println("\033[1;32m----------------------------\033[0m");

        // Perform signin to generate JWT token using the raw password
        return signin(appUserDTO.getEmail(), rawPassword);
    }

    /**
     * Sign in a user.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return the signed-in user
     */
    public AppUserDTO signin(String email, String password) {
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            if (passwordEncoder.matches(password, appUser.getPassword())) {
                String token = jwtUtil.generateToken(appUser.getEmail());
                if (isDevProfileActive()) {
                    logger.info("\033[1;33mGenerated JWT Token: {}\033[0m", token); // Yellow color
                }
                AppUserDTO userDTO = appUserMapper.toDTO(appUser);
                userDTO.setToken(token); // Setting the token in AppUserDTO

                // Logging
                System.out.println("\033[1;32m----------------------------\033[0m");
                System.out.println("\033[1;32m[UserService] Signin successful for user: " + email + "\033[0m");
                System.out.println("\033[1;32mGenerated JWT Token: " + token + "\033[0m");
                System.out.println("\033[1;32m----------------------------\033[0m");

                return userDTO;
            } else {
                // Logging password mismatch
                System.out.println("\033[1;31m----------------------------\033[0m");
                System.out.println("\033[1;31m[UserService] Signin failed for user: " + email + " - Incorrect password\033[0m");
                System.out.println("\033[1;31m----------------------------\033[0m");
            }
        } else {
            // Logging user not found
            System.out.println("\033[1;31m----------------------------\033[0m");
            System.out.println("\033[1;31m[UserService] Signin failed - User not found: " + email + "\033[0m");
            System.out.println("\033[1;31m----------------------------\033[0m");
        }
        throw new IllegalArgumentException("Invalid email or password");
    }

    /**
     * Resend verification email to the user.
     *
     * @param email the user's email
     */
    public void resendVerificationEmail(String email) {
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            verificationService.regenerateAndSendVerificationToken(appUser);

            // Logging
            System.out.println("\033[1;32m----------------------------\033[0m");
            System.out.println("\033[1;32m[UserService] Resent verification email to user: " + email + "\033[0m");
            System.out.println("\033[1;32m----------------------------\033[0m");
        } else {
            throw new IllegalArgumentException("User with email " + email + " does not exist");
        }
    }

    /**
     * Find user by phone number.
     *
     * @param phone the user's phone number
     * @return the user entity
     */
    public AppUser findByPhone(String phone) {
        return appUserRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("User with phone " + phone + " does not exist"));
    }

    /**
     * Check if the dev profile is active.
     *
     * @return true if dev profile is active, false otherwise
     */
    private boolean isDevProfileActive() {
        return Arrays.asList(env.getActiveProfiles()).contains("dev");
    }

    /**
     * Find user by email.
     *
     * @param email the user's email
     * @return the user DTO
     */
    public AppUserDTO findByEmail(String email) {
        Optional<AppUser> userOptional = appUserRepository.findByEmail(email);
        return userOptional.map(appUserMapper::toDTO).orElse(null);
    }

    /**
     * Find user entity by email.
     *
     * @param email the user's email
     * @return the user entity
     */
    public AppUser findEntityByEmail(String email) {
        return appUserRepository.findByEmail(email).orElse(null);
    }

    /**
     * Sign in with OTP.
     *
     * @param email the user's email
     * @param otp   the OTP
     * @return the user DTO
     */
    public AppUserDTO signinWithOtp(String email, String otp) {
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            if (otpService.validateOtp(appUser, otp)) {
                otpService.invalidateOtp(appUser);
                AppUserDTO userDTO = appUserMapper.toDTO(appUser);
                String token = jwtUtil.generateToken(appUser.getEmail());
                userDTO.setToken(token); // Setting the token in AppUserDTO

                // Logging
                System.out.println("\033[1;32m----------------------------\033[0m");
                System.out.println("\033[1;32m[UserService] Signin with OTP successful for user: " + email + "\033[0m");
                System.out.println("\033[1;32mGenerated JWT Token: " + token + "\033[0m");
                System.out.println("\033[1;32m----------------------------\033[0m");

                return userDTO;
            } else {
                throw new IllegalArgumentException("Invalid OTP or OTP expired");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    /**
     * Logout a user.
     *
     * @param token the JWT token
     */
    public void logout(String token) {
        AppUser appUser = jwtUtil.extractUserFromToken(token);
        otpService.invalidateOtp(appUser);
    }
}
