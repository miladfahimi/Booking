package com.tennistime.profile.application.service;

import com.tennistime.profile.application.dto.*;
import com.tennistime.profile.application.mapper.UserProfileMapper;
import com.tennistime.profile.application.mapper.UserSubscriptionMapper;
import com.tennistime.profile.domain.model.UserBookingHistory;
import com.tennistime.profile.domain.model.UserProfile;
import com.tennistime.profile.domain.model.UserSubscription;
import com.tennistime.profile.domain.repository.UserBookingHistoryRepository;
import com.tennistime.profile.domain.repository.UserProfileRepository;
import com.tennistime.profile.domain.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInitializationService {

    private final UserProfileRepository userProfileRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserBookingHistoryRepository userBookingHistoryRepository;
    private final UserProfileMapper userProfileMapper;
    private final UserSubscriptionMapper userSubscriptionMapper;

    private static final Logger LOGGER = Logger.getLogger(UserInitializationService.class.getName());

    @Transactional
    public UserInitializationResponseDTO initializeUserEntities(UserInitializationRequestDTO request) {
        UUID userId = request.getUserProfileDTO().getUserId();
        String email = request.getUserProfileDTO().getEmail();
        String phoneNumber = request.getUserProfileDTO().getPhoneNumber();

        // Check if user profile is already initiated
        Optional<UserProfile> existingUserProfileOpt = userProfileRepository.findByUserId(userId);
        if (existingUserProfileOpt.isPresent()) {
            UserProfile existingUserProfile = existingUserProfileOpt.get();
            if (existingUserProfile.isUserProfilesInitiated()) {
                LOGGER.info("\u001B[31mUser profiles already initiated for userId: " + userId + "\u001B[0m");
                throw new IllegalStateException("User profiles initiated already!");
            } else {
                existingUserProfile.setUserProfilesInitiated(true);
                userProfileRepository.save(existingUserProfile);
            }
        }

        // Initialize user profile
        UserProfile userProfile = initializeUserProfile(userId, email, phoneNumber);
        UserProfileDTO userProfileDTO = userProfileMapper.toDTO(userProfile);

        // Initialize user subscription
        UserSubscription userSubscription = initializeUserSubscription(userId);
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDTO(userSubscription);

        // Initialize user booking history
        List<UserBookingHistoryDTO> userBookingHistoryDTOList = initializeUserBookingHistory(userId);

        // Construct response DTO
        UserInitializationResponseDTO responseDTO = new UserInitializationResponseDTO(
                userId,
                true,
                userProfileDTO,
                userSubscriptionDTO,
                userBookingHistoryDTOList
        );

        return responseDTO;
    }

    public Optional<UserInitializationResponseDTO> getUserInitializationByUserId(UUID userId) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(userId);
        if (userProfileOpt.isPresent()) {
            UserProfile userProfile = userProfileOpt.get();
            UserProfileDTO userProfileDTO = userProfileMapper.toDTO(userProfile);

            Optional<UserSubscription> userSubscriptionOpt = userSubscriptionRepository.findByUserId(userId);
            UserSubscriptionDTO userSubscriptionDTO = userSubscriptionOpt
                    .map(userSubscriptionMapper::toDTO)
                    .orElse(null);

            List<UserBookingHistoryDTO> userBookingHistoryDTOList = userBookingHistoryRepository.findByUserId(userId).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            UserInitializationResponseDTO responseDTO = new UserInitializationResponseDTO(
                    userId,
                    userProfile.isUserProfilesInitiated(),
                    userProfileDTO,
                    userSubscriptionDTO,
                    userBookingHistoryDTOList
            );
            return Optional.of(responseDTO);
        } else {
            return Optional.empty();
        }
    }

    private UserProfile initializeUserProfile(UUID userId, String email, String phoneNumber) {
        return userProfileRepository.findByUserId(userId).orElseGet(() -> {
            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(userId);
            userProfile.setEmail(email);
            userProfile.setPhone(phoneNumber);
            userProfile.setFirstName(null); // Now null by default
            userProfile.setLastName(null);  // Now null by default
            userProfile.setAddress(null);   // Now null by default
            userProfile.setProfilePictureUrl(null); // Now null by default
            userProfile.setPreferences(null); // Now null by default
            userProfile.setDateOfBirth(null);
            userProfile.setUserProfilesInitiated(true);
            LOGGER.info("\u001B[32mUserProfile created for userId: " + userId + "\u001B[0m");
            return userProfileRepository.save(userProfile);
        });
    }

    private UserSubscription initializeUserSubscription(UUID userId) {
        return userSubscriptionRepository.findByUserId(userId).orElseGet(() -> {
            UserSubscription userSubscription = new UserSubscription();
            userSubscription.setUserId(userId);
            userSubscription.setSubscriptionPlan(null); // Now null by default
            userSubscription.setStatus("Inactive");
            userSubscription.setStartDate(null);
            userSubscription.setEndDate(null);
            LOGGER.info("\u001B[32mUserSubscription created for userId: " + userId + "\u001B[0m");
            return userSubscriptionRepository.save(userSubscription);
        });
    }

    private List<UserBookingHistoryDTO> initializeUserBookingHistory(UUID userId) {
        return userBookingHistoryRepository.findByUserId(userId).stream().map(booking -> {
            UserBookingHistoryDTO dto = new UserBookingHistoryDTO();
            dto.setId(booking.getId());
            dto.setUserId(userId);
            dto.setBookingDate(null); // Now null by default
            dto.setStatus(null); // Now null by default
            dto.setBookingDatePersian(null); // Now null by default
            return dto;
        }).collect(Collectors.toList());
    }

    private UserBookingHistoryDTO convertToDTO(UserBookingHistory userBookingHistory) {
        UserBookingHistoryDTO dto = new UserBookingHistoryDTO();
        dto.setId(userBookingHistory.getId());
        dto.setUserId(userBookingHistory.getUserId());
        dto.setBookingDate(userBookingHistory.getBookingDate().toString());
        dto.setStatus(userBookingHistory.getStatus());
        dto.setBookingDatePersian(userBookingHistory.getBookingDatePersian() != null ? userBookingHistory.getBookingDatePersian().toString() : null);
        return dto;
    }
}
