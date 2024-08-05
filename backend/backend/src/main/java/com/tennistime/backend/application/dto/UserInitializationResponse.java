package com.tennistime.backend.application.dto;

import com.tennistime.backend.application.util.PersianDateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Initialization Response Data Transfer Object")
public class UserInitializationResponse {

    @Schema(description = "User profile information")
    private UserProfileDTO userProfile;

    @Schema(description = "User subscription information")
    private UserSubscriptionDTO userSubscription;

    @Schema(description = "User booking history information")
    private UserBookingHistoryDTO userBookingHistory;

    @Schema(description = "Start date of the subscription in Persian calendar", example = "1402-10-11")
    private String startDatePersian = "Does not filled by User";

    @Schema(description = "End date of the subscription in Persian calendar", example = "1403-10-11")
    private String endDatePersian = "Does not filled by User";

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
