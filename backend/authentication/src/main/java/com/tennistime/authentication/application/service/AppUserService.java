package com.tennistime.authentication.application.service;


import com.tennistime.authentication.application.dto.AppUserDTO;
import com.tennistime.authentication.application.mapper.AppUserMapper;
import com.tennistime.authentication.domain.model.AppUser;
import com.tennistime.authentication.domain.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
