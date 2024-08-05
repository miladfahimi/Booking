package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.UserProfile;
import com.tennistime.backend.domain.model.UserSubscription;
import com.tennistime.backend.domain.repository.UserProfileRepository;
import com.tennistime.backend.domain.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserInitializationService {

    private final UserProfileRepository userProfileRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    private static final Logger LOGGER = Logger.getLogger(UserInitializationService.class.getName());

    @Transactional
    public UserProfile initializeUserProfile(Long userId, String email, String phone) {
        Optional<UserProfile> existingProfileOpt = userProfileRepository.findByUserId(userId);
        UserProfile userProfile;

        if (existingProfileOpt.isPresent()) {
            userProfile = existingProfileOpt.get();
            if (userProfile.getEmail() == null || !userProfile.getEmail().equals(email)) {
                userProfile.setEmail(email); // Ensure email is set/updated
            }
            if (userProfile.getPhoneNumber() == null || !userProfile.getPhoneNumber().equals(phone)) {
                userProfile.setPhoneNumber(phone); // Ensure phone is set/updated
            }
        } else {
            userProfile = new UserProfile();
            userProfile.setUserId(userId);
            userProfile.setEmail(email);
            userProfile.setPhoneNumber(phone);
            userProfile.setFirstName("Default First Name");
            userProfile.setLastName("Default Last Name");
            userProfile.setAddress("Default Address");
            userProfile.setDateOfBirth(LocalDate.of(1990, 1, 1)); // Default date of birth
            userProfile.setProfilePicture("/images/default-profile.jpg");
            userProfile.setPreferences("Default Preferences");
            userProfileRepository.save(userProfile);
            LOGGER.info("\u001B[32mUserProfile created for userId: " + userId + "\u001B[0m");
        }

        return userProfile;
    }

    // If you need to initialize UserBookingHistory, make sure you have a valid court object.
    // If you don't have a court object, skip creating UserBookingHistory for now.
        /*
        if (!userBookingHistoryRepository.existsByUserId(userId)) {
            UserBookingHistory userBookingHistory = new UserBookingHistory();
            userBookingHistory.setUserId(userId);
            // userBookingHistory.setCourt(court); // Ensure this is set correctly
            userBookingHistoryRepository.save(userBookingHistory);
            LOGGER.info("\u001B[34mUserBookingHistory created for userId: " + userId + "\u001B[0m");
        }
        */


    @Transactional
    public UserSubscription initializeUserSubscription(Long userId) {
        Optional<UserSubscription> existingSubscriptionOpt = userSubscriptionRepository.findByUserId(userId);
        UserSubscription userSubscription;

        if (existingSubscriptionOpt.isPresent()) {
            userSubscription = existingSubscriptionOpt.get();
        } else {
            userSubscription = new UserSubscription();
            userSubscription.setUserId(userId);
            userSubscription.setSubscriptionPlan("Default Plan");
            userSubscription.setStartDate(LocalDate.of(2024, 1, 1));
            userSubscription.setEndDate(LocalDate.of(2024, 12, 31));
            userSubscription.setStatus("Active");
            userSubscriptionRepository.save(userSubscription);
            LOGGER.info("\u001B[36mUserSubscription created for userId: " + userId + "\u001B[0m");
        }

        return userSubscription;
    }
}
