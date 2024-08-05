package com.tennistime.backend.application.service;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.backend.application.dto.UserInitializationResponse;
import com.tennistime.backend.application.dto.UserProfileDTO;
import com.tennistime.backend.application.dto.UserSubscriptionDTO;
import com.tennistime.backend.application.mapper.UserProfileMapper;
import com.tennistime.backend.application.mapper.UserSubscriptionMapper;
import com.tennistime.backend.domain.model.UserProfile;
import com.tennistime.backend.domain.model.UserSubscription;
import com.tennistime.backend.domain.repository.UserProfileRepository;
import com.tennistime.backend.domain.repository.UserSubscriptionRepository;
import com.tennistime.backend.application.util.PersianDateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserInitializationService {

    private final UserProfileRepository userProfileRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserProfileMapper userProfileMapper;
    private final UserSubscriptionMapper userSubscriptionMapper;

    private static final Logger LOGGER = Logger.getLogger(UserInitializationService.class.getName());

    @Transactional
    public UserInitializationResponse initializeUserEntities(Long userId, String email, String phone) {
        UserProfile userProfile = initializeUserProfile(userId, email, phone);
        UserSubscription userSubscription = initializeUserSubscription(userId);

        UserProfileDTO userProfileDTO = userProfileMapper.toDTO(userProfile);
        userProfileDTO.setDateOfBirthPersian(PersianDateUtil.localDateToString(userProfile.getDateOfBirth()));

        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDTO(userSubscription);
        userSubscriptionDTO.setStartDatePersian(PersianDateUtil.localDateToString(userSubscription.getStartDate()));
        userSubscriptionDTO.setEndDatePersian(PersianDateUtil.localDateToString(userSubscription.getEndDate()));

        return new UserInitializationResponse(
                userProfileDTO,
                userSubscriptionDTO,
                null,
                PersianDateUtil.localDateToString(userSubscription.getStartDate()),
                PersianDateUtil.localDateToString(userSubscription.getEndDate())
        );
    }

    @Transactional
    private UserProfile initializeUserProfile(Long userId, String email, String phone) {
        Optional<UserProfile> existingUserProfile = userProfileRepository.findByUserId(userId);
        UserProfile userProfile;
        if (existingUserProfile.isPresent()) {
            userProfile = existingUserProfile.get();
        } else {
            userProfile = new UserProfile();
            userProfile.setUserId(userId);
            userProfile.setEmail(email != null ? email : "Does not filled by User");
            userProfile.setPhoneNumber(phone != null ? phone : "Does not updated by user");
            userProfile.setFirstName("Does not filled by User");
            userProfile.setLastName("Does not filled by User");
            userProfile.setAddress("Does not filled by User");
            userProfile.setPreferences("Does not filled by User");
            userProfile.setDateOfBirth(null);
            userProfile.setProfilePicture("Does not filled by User");
            userProfile = userProfileRepository.save(userProfile);
            LOGGER.info("\u001B[32mUserProfile created for userId: " + userId + "\u001B[0m");
        }
        return userProfile;
    }

    @Transactional
    private UserSubscription initializeUserSubscription(Long userId) {
        Optional<UserSubscription> existingUserSubscription = userSubscriptionRepository.findByUserId(userId);
        UserSubscription userSubscription;
        if (existingUserSubscription.isPresent()) {
            userSubscription = existingUserSubscription.get();
        } else {
            userSubscription = new UserSubscription();
            userSubscription.setUserId(userId);
            userSubscription.setSubscriptionPlan("Does not filled by User");
            userSubscription.setStatus("Inactive");
            userSubscription = userSubscriptionRepository.save(userSubscription);
            LOGGER.info("\u001B[32mUserSubscription created for userId: " + userId + "\u001B[0m");
        }
        return userSubscription;
    }
}
