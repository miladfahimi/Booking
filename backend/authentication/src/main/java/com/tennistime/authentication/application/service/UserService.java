package com.tennistime.authentication.application.service;

import com.tennistime.authentication.application.dto.UserDTO;
import com.tennistime.authentication.application.mapper.AppUserMapper;
import com.tennistime.authentication.domain.model.User;
import com.tennistime.authentication.domain.repository.UserRepository;
import com.tennistime.authentication.infrastructure.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private UserRepository userRepository;

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

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Sign up a new user.
     *
     * @param userDTO the user to sign up
     * @return the signed-up user
     */
    public UserDTO signup(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userDTO.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        // Save the raw password for later login
        String rawPassword = userDTO.getPassword();

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = appUserMapper.toEntity(userDTO);
        user = userRepository.save(user);
        verificationService.sendVerificationEmail(user);

        // Logging
        System.out.println("\033[1;32m----------------------------\033[0m");
        System.out.println("\033[1;32m[UserService] Signup successful for user: " + user.getEmail() + "\033[0m");
        System.out.println("\033[1;32m----------------------------\033[0m");

        // Perform signin to generate JWT token using the raw password
        return signin(userDTO.getEmail(), rawPassword);
    }

    /**
     * Sign in a user.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return the signed-in user
     */
    public UserDTO signin(String email, String password) {
        Optional<User> appUserOptional = userRepository.findByEmail(email);
        if (appUserOptional.isPresent()) {
            User user = appUserOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                String token = jwtUtil.generateToken(userDetails);
                if (isDevProfileActive()) {
                    logger.info("\033[1;33mGenerated JWT Token: {}\033[0m", token); // Yellow color
                }
                UserDTO userDTO = appUserMapper.toDTO(user);
                userDTO.setToken(token); // Setting the token in UserDTO

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
        Optional<User> appUserOptional = userRepository.findByEmail(email);
        if (appUserOptional.isPresent()) {
            User user = appUserOptional.get();
            verificationService.regenerateAndSendVerificationToken(user);

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
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone)
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
    public UserDTO findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(appUserMapper::toDTO).orElse(null);
    }

    /**
     * Find user entity by email.
     *
     * @param email the user's email
     * @return the user entity
     */
    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Sign in with OTP.
     *
     * @param email the user's email
     * @param otp   the OTP
     * @return the user DTO
     */
    public UserDTO signinWithOtp(String email, String otp) {
        Optional<User> appUserOptional = userRepository.findByEmail(email);
        if (appUserOptional.isPresent()) {
            User user = appUserOptional.get();
            if (otpService.validateOtp(user, otp)) {
                otpService.invalidateOtp(user);
                UserDTO userDTO = appUserMapper.toDTO(user);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                String token = jwtUtil.generateToken(userDetails);
                userDTO.setToken(token); // Setting the token in UserDTO

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
        User user = jwtUtil.extractUserFromToken(token);
        otpService.invalidateOtp(user);
    }
}
