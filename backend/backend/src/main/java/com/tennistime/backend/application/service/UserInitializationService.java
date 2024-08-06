package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.userDetails.*;
import com.tennistime.backend.application.mapper.UserProfileMapper;
import com.tennistime.backend.application.mapper.UserSubscriptionMapper;
import com.tennistime.backend.domain.model.UserBookingHistory;
import com.tennistime.backend.domain.model.UserProfile;
import com.tennistime.backend.domain.model.UserSubscription;
import com.tennistime.backend.domain.repository.UserProfileRepository;
import com.tennistime.backend.domain.repository.UserSubscriptionRepository;
import com.tennistime.backend.domain.repository.UserBookingHistoryRepository;
import com.tennistime.backend.application.util.PersianDateUtil;
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
                userBookingHistoryDTOList,
                userSubscriptionDTO.getStartDatePersian(),
                userSubscriptionDTO.getEndDatePersian()
        );

        return responseDTO;
    }

    private UserProfile initializeUserProfile(UUID userId, String email, String phoneNumber) {
        return userProfileRepository.findByUserId(userId).orElseGet(() -> {
            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(userId);
            userProfile.setEmail(email);
            userProfile.setPhoneNumber(phoneNumber);
            userProfile.setFirstName("Does not filled by User");
            userProfile.setLastName("Does not filled by User");
            userProfile.setAddress("Does not filled by User");
            userProfile.setProfilePicture("Does not filled by User");
            userProfile.setPreferences("Does not filled by User");
            userProfile.setDateOfBirth(null);
            LOGGER.info("\u001B[32mUserProfile created for userId: " + userId + "\u001B[0m");
            return userProfileRepository.save(userProfile);
        });
    }

    private UserSubscription initializeUserSubscription(UUID userId) {
        return userSubscriptionRepository.findByUserId(userId).orElseGet(() -> {
            UserSubscription userSubscription = new UserSubscription();
            userSubscription.setUserId(userId);
            userSubscription.setSubscriptionPlan("Does not filled by User");
            userSubscription.setStatus("Inactive");
            userSubscription.setStartDate(null);
            userSubscription.setEndDate(null);
            LOGGER.info("\u001B[32mUserSubscription created for userId: " + userId + "\u001B[0m");
            return userSubscriptionRepository.save(userSubscription);
        });
    }


    private List<UserBookingHistoryDTO> initializeUserBookingHistory(UUID userId) {
        List<UserBookingHistory> userBookingHistoryList = userBookingHistoryRepository.findByUserId(userId);
        return userBookingHistoryList.stream().map(booking -> {
            UserBookingHistoryDTO dto = new UserBookingHistoryDTO();
            dto.setId(booking.getId());
            dto.setUserId(userId);
            dto.setBookingDate("Does not filled by User");
            dto.setStatus("Does not filled by User");
            dto.setCourtId(null);
            dto.setBookingDatePersian("Does not filled by User");
            return dto;
        }).collect(Collectors.toList());
    }
}
