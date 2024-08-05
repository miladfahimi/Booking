package com.tennistime.backend.application.dto;

import com.tennistime.backend.application.util.PersianDateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInitializationResponse {
    private UserProfileDTO userProfile;
    private UserSubscriptionDTO userSubscription;
    private UserBookingHistoryDTO userBookingHistory;
    private String startDatePersian;
    private String endDatePersian;

    public UserInitializationResponse(UserProfileDTO userProfile, UserSubscriptionDTO userSubscription, UserBookingHistoryDTO userBookingHistory) {
        this.userProfile = userProfile != null ? userProfile : new UserProfileDTO();
        this.userSubscription = userSubscription != null ? userSubscription : new UserSubscriptionDTO();
        this.userBookingHistory = userBookingHistory != null ? userBookingHistory : new UserBookingHistoryDTO();

        this.startDatePersian = userSubscription != null && userSubscription.getStartDate() != null
                ? PersianDateUtil.localDateToString(LocalDate.parse(userSubscription.getStartDate()))
                : "Does not filled by User";
        this.endDatePersian = userSubscription != null && userSubscription.getEndDate() != null
                ? PersianDateUtil.localDateToString(LocalDate.parse(userSubscription.getEndDate()))
                : "Does not filled by User";
    }
}
