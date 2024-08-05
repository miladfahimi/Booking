package com.tennistime.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionDTO {
    private Long id;
    private Long userId;
    private String subscriptionPlan = "Does not filled by User";
    private String status = "Inactive";
    private String startDate = "Does not filled by User";
    private String endDate = "Does not filled by User";
    private String startDatePersian = "Does not filled by User";
    private String endDatePersian = "Does not filled by User";
}
