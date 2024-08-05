package com.tennistime.backend.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Subscription Data Transfer Object")
public class UserSubscriptionDTO {

    @Schema(description = "Unique identifier of the user subscription", example = "1")
    private Long id;

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long userId;

    @Schema(description = "Subscription plan of the user", example = "Premium")
    private String subscriptionPlan = "Does not filled by User";

    @Schema(description = "Status of the subscription", example = "Active")
    private String status = "Inactive";

    @Schema(description = "Start date of the subscription", example = "2024-01-01")
    private String startDate = "Does not filled by User";

    @Schema(description = "End date of the subscription", example = "2024-12-31")
    private String endDate = "Does not filled by User";

    @Schema(description = "Start date of the subscription in Persian calendar", example = "1402-10-11")
    private String startDatePersian = "Does not filled by User";

    @Schema(description = "End date of the subscription in Persian calendar", example = "1403-10-11")
    private String endDatePersian = "Does not filled by User";
}
