package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.AppUserDTO;
import com.tennistime.backend.application.mapper.AppUserMapper;
import com.tennistime.backend.domain.model.AppUser;
import com.tennistime.backend.domain.repository.AppUserRepository;
import com.tennistime.backend.exception.EmailAlreadyInUseException;
import com.tennistime.backend.exception.InvalidVerificationTokenException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    // Temporary token storage for demonstration
    private String verificationToken;
    private Long verificationUserId;

    private final Map<String, Long> verificationTokens = new HashMap<>();

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
            throw new EmailAlreadyInUseException("Email already in use");
        }
        appUserDTO.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        appUser.setRole("USER");
        appUser = appUserRepository.save(appUser);
        sendVerificationEmail(appUser);
        return appUserMapper.toDTO(appUser);
    }

    public AppUserDTO signin(String email, String password) {
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            if (passwordEncoder.matches(password, appUser.getPassword())) {
                return appUserMapper.toDTO(appUser);
            }
        }
        throw new IllegalArgumentException("Invalid email or password");
    }

    private void sendVerificationEmail(AppUser appUser) {
        verificationToken = UUID.randomUUID().toString();
        verificationUserId = appUser.getId();
        String verificationLink = "http://localhost:8080/api/v1/users/verify?token=" + verificationToken;

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(appUser.getEmail());
            helper.setSubject("Email Verification");
            helper.setText("Please click the following link to verify your email: <a href=\"" + verificationLink + "\">Verify Email</a>", true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void verifyEmail(String token) {
        if (!token.equals(verificationToken)) {
            throw new IllegalArgumentException("Invalid verification token");
        }
        Optional<AppUser> appUserOptional = appUserRepository.findById(verificationUserId);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            appUser.setRole("VERIFIED_USER");
            appUserRepository.save(appUser);
        }
    }
}
