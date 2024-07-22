package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.AppUserDTO;
import com.tennistime.backend.application.mapper.AppUserMapper;
import com.tennistime.backend.domain.model.AppUser;
import com.tennistime.backend.domain.repository.AppUserRepository;
import com.tennistime.backend.infrastructure.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<AppUserDTO> findAll() {
        return appUserRepository.findAll().stream()
                .map(appUserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AppUserDTO save(AppUserDTO appUserDTO) {
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser = appUserRepository.save(appUser);
        return appUserMapper.toDTO(appUser);
    }

    public AppUserDTO findById(Long id) {
        return appUserRepository.findById(id)
                .map(appUserMapper::toDTO)
                .orElse(null);
    }

    public void deleteById(Long id) {
        appUserRepository.deleteById(id);
    }

    public AppUserDTO signup(AppUserDTO appUserDTO) {
        if (appUserRepository.findByEmail(appUserDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (appUserDTO.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        appUserDTO.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        appUser.setRole("USER");
        appUser = appUserRepository.save(appUser);
        verificationService.sendVerificationEmail(appUser);

        // Logging
        System.out.println("\033[1;32m----------------------------\033[0m");
        System.out.println("\033[1;32m[UserService] Signup successful for user: " + appUser.getEmail() + "\033[0m");
        System.out.println("\033[1;32m----------------------------\033[0m");

        // Perform signin to generate JWT token
        return signin(appUserDTO.getEmail(), appUserDTO.getPassword());
    }

    public AppUserDTO signin(String email, String password) {
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            if (passwordEncoder.matches(password, appUser.getPassword())) {
                String token = jwtUtil.generateToken(appUser.getEmail());
                if (isDevProfileActive()) {
                    logger.info("\033[1;33m----------------------------\033[0m");
                    logger.info("\033[1;33mGenerated JWT Token: {}\033[0m", token); // Yellow color
                    logger.info("\033[1;33m----------------------------\033[0m");
                }
                AppUserDTO userDTO = appUserMapper.toDTO(appUser);
                userDTO.setToken(token); // Setting the token in AppUserDTO

                // Logging
                System.out.println("\033[1;32m----------------------------\033[0m");
                System.out.println("\033[1;32m[UserService] Signin successful for user: " + email + "\033[0m");
                System.out.println("\033[1;32mGenerated JWT Token: " + token + "\033[0m");
                System.out.println("\033[1;32m----------------------------\033[0m");

                return userDTO;
            }
        }
        throw new IllegalArgumentException("Invalid email or password");
    }

    private boolean isDevProfileActive() {
        return Arrays.asList(env.getActiveProfiles()).contains("dev");
    }
}
