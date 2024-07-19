package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.AppUserDTO;
import com.tennistime.backend.application.mapper.AppUserMapper;
import com.tennistime.backend.domain.model.AppUser;
import com.tennistime.backend.domain.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationService verificationService;

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
        appUserDTO.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        appUser.setRole("USER");
        appUser = appUserRepository.save(appUser);
        verificationService.sendVerificationEmail(appUser);
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
}
