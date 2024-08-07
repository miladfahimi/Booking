package com.tennistime.backend.application.service.userDetails;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.backend.application.dto.userDetails.UserProfileDTO;
import com.tennistime.backend.application.mapper.userDetails.UserProfileMapper;
import com.tennistime.backend.domain.model.userDetails.UserProfile;
import com.tennistime.backend.domain.repository.userDetails.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public Optional<UserProfileDTO> getUserProfileByUserId(UUID userId) {
        logger.info("\033[0;34mFetching user profile for user ID: {}\033[0m", userId);
        return userProfileRepository.findByUserId(userId)
                .map(this::convertToDTO);
    }

    public UserProfileDTO createUserProfile(UserProfileDTO userProfileDTO) {
        logger.info("\033[0;32mCreating new user profile: {}\033[0m", userProfileDTO);
        UserProfile userProfile = convertToEntity(userProfileDTO);
        UserProfile savedUserProfile = userProfileRepository.save(userProfile);
        return convertToDTO(savedUserProfile);
    }

    public Optional<UserProfileDTO> updateUserProfile(UUID userId, UserProfileDTO updatedProfileDTO) {
        logger.info("\033[0;33mUpdating user profile for user ID: {}\033[0m", userId);
        return userProfileRepository.findByUserId(userId)
                .map(existingProfile -> {
                    UserProfile updatedProfile = convertToEntity(updatedProfileDTO);
                    updatedProfile.setId(existingProfile.getId());
                    updatedProfile.setUserId(userId); // Ensure the userId is set
                    UserProfile savedUserProfile = userProfileRepository.save(updatedProfile);
                    return convertToDTO(savedUserProfile);
                });
    }

    public void deleteUserProfileByUserId(UUID userId) {
        logger.info("\033[0;31mDeleting user profile for user ID: {}\033[0m", userId);
        userProfileRepository.findByUserId(userId)
                .ifPresent(userProfile -> userProfileRepository.deleteById(userProfile.getId()));
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
