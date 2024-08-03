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

    public UserDTO signup(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            logger.error("\033[1;35mSignup attempt with already registered email: {}\033[0m", userDTO.getEmail());
            throw new IllegalArgumentException("Email already in use");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            logger.error("\033[1;35mSignup attempt with empty password for email: {}\033[0m", userDTO.getEmail());
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        String rawPassword = userDTO.getPassword();
        userDTO.setPassword(passwordEncoder.encode(rawPassword));
        User user = appUserMapper.toEntity(userDTO);
        user = userRepository.save(user);

        try {
            verificationService.sendVerificationEmail(user);
        } catch (Exception e) {
            logger.error("\033[1;35mFailed to send verification email for user: {}\033[0m", user.getEmail());
            throw new IllegalArgumentException("The mail server is down. Please try again later.");
        }

        logger.info("\033[1;35mSignup successful for user: {}\033[0m", user.getEmail());

        return signin(userDTO.getEmail(), rawPassword);
    }

    public UserDTO signin(String emailOrPhone, String password) {
        Optional<User> appUserOptional = userRepository.findByEmail(emailOrPhone)
                .or(() -> userRepository.findByPhone(emailOrPhone));

        if (appUserOptional.isPresent()) {
            User user = appUserOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(emailOrPhone);
                String token = jwtUtil.generateToken(userDetails);
                if (isDevProfileActive()) {
                    logger.info("\033[1;32mGenerated JWT Token: {}\033[0m", token);
                }
                UserDTO userDTO = appUserMapper.toDTO(user);
                userDTO.setToken(token);

                logger.info("\033[1;32mSignin successful for user: {}\033[0m", emailOrPhone);
                return userDTO;
            } else {
                logger.error("\033[1;32mSignin failed for user: {} - Incorrect password\033[0m", emailOrPhone);
            }
        } else {
            logger.error("\033[1;32mSignin failed - User not found: {}\033[0m", emailOrPhone);
        }
        throw new IllegalArgumentException("Invalid email/phone or password");
    }

    public void resendVerificationEmail(String email) {
        Optional<User> appUserOptional = userRepository.findByEmail(email);
        if (appUserOptional.isPresent()) {
            User user = appUserOptional.get();
            try {
                verificationService.regenerateAndSendVerificationToken(user);
                logger.info("\033[1;34mResent verification email to user: {}\033[0m", email);
            } catch (Exception e) {
                logger.error("\033[1;34mFailed to resend verification email for user: {}\033[0m", email);
                throw new IllegalArgumentException("The mail server is down. Please try again later.");
            }
        } else {
            throw new IllegalArgumentException("User with email " + email + " does not exist");
        }
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("User with phone " + phone + " does not exist"));
    }

    private boolean isDevProfileActive() {
        return Arrays.asList(env.getActiveProfiles()).contains("dev");
    }

    public UserDTO findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(appUserMapper::toDTO).orElse(null);
    }

    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserDTO signinWithOtp(String email, String otp) {
        Optional<User> appUserOptional = userRepository.findByEmail(email);
        if (appUserOptional.isPresent()) {
            User user = appUserOptional.get();
            if (otpService.validateOtp(user, otp)) {
                otpService.invalidateOtp(user);
                UserDTO userDTO = appUserMapper.toDTO(user);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                String token = jwtUtil.generateToken(userDetails);
                userDTO.setToken(token);

                logger.info("\033[1;36mSignin with OTP successful for user: {}\033[0m", email);
                return userDTO;
            } else {
                throw new IllegalArgumentException("Invalid OTP or OTP expired");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public void logout(String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        otpService.invalidateOtp(user);

        logger.info("\033[1;31mLogout successful for user: {}\033[0m", username);
    }
}
