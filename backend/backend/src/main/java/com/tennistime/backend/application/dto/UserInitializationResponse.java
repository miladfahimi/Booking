package com.tennistime.backend.application.dto;

import com.tennistime.backend.domain.model.UserProfile;
import com.tennistime.backend.domain.model.UserSubscription;
import com.github.mfathi91.time.PersianDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInitializationResponse {
    private UserProfile userProfile;
    private UserSubscription userSubscription;

    private String startDatePersian;
    private String endDatePersian;

    public UserInitializationResponse(UserProfile userProfile, UserSubscription userSubscription) {
        this.userProfile = userProfile != null ? userProfile : new UserProfile();
        this.userSubscription = userSubscription != null ? userSubscription : new UserSubscription();

        this.startDatePersian = userSubscription != null && userSubscription.getStartDate() != null
                ? PersianDate.fromGregorian(userSubscription.getStartDate()).toString()
                : null;
        this.endDatePersian = userSubscription != null && userSubscription.getEndDate() != null
                ? PersianDate.fromGregorian(userSubscription.getEndDate()).toString()
                : null;
    }
}
