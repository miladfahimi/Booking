package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.UserProfileDTO;
import com.tennistime.backend.application.mapper.UserProfileMapper;
import com.tennistime.backend.domain.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    public UserProfileDTO findByEmail(String email) {
        return userProfileRepository.findByEmail(email)
                .map(userProfileMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
