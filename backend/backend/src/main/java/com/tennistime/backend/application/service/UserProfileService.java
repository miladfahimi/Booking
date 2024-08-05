package com.tennistime.backend.application.service;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.backend.application.dto.userDetails.UserProfileDTO;
import com.tennistime.backend.application.mapper.UserProfileMapper;
import com.tennistime.backend.domain.model.UserProfile;
import com.tennistime.backend.domain.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public Optional<UserProfileDTO> getUserProfile(Long id) {
        return userProfileRepository.findById(id)
                .map(this::convertToDTO);
    }

    public UserProfileDTO createUserProfile(UserProfileDTO userProfileDTO) {
        UserProfile userProfile = convertToEntity(userProfileDTO);
        UserProfile savedUserProfile = userProfileRepository.save(userProfile);
        return convertToDTO(savedUserProfile);
    }

    public Optional<UserProfileDTO> updateUserProfile(Long id, UserProfileDTO updatedProfileDTO) {
        return userProfileRepository.findById(id)
                .map(existingProfile -> {
                    UserProfile updatedProfile = convertToEntity(updatedProfileDTO);
                    updatedProfile.setId(existingProfile.getId());
                    UserProfile savedUserProfile = userProfileRepository.save(updatedProfile);
                    return convertToDTO(savedUserProfile);
                });
    }

    public void deleteUserProfile(Long id) {
        userProfileRepository.deleteById(id);
    }

    private UserProfileDTO convertToDTO(UserProfile userProfile) {
        UserProfileDTO dto = userProfileMapper.toDTO(userProfile);
        if (userProfile.getDateOfBirth() != null) {
            dto.setDateOfBirthPersian(PersianDate.fromGregorian(userProfile.getDateOfBirth()).toString());
        }
        return dto;
    }

    private UserProfile convertToEntity(UserProfileDTO userProfileDTO) {
        UserProfile userProfile = userProfileMapper.toEntity(userProfileDTO);
        if (userProfileDTO.getDateOfBirthPersian() != null) {
            userProfile.setDateOfBirth(PersianDate.parse(userProfileDTO.getDateOfBirthPersian()).toGregorian());
        }
        return userProfile;
    }
}
