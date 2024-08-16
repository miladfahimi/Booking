package com.tennistime.profile.domain.model;

import com.github.mfathi91.time.PersianDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_subscription")
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    private String subscriptionPlan;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    @Transient
    private PersianDate startDatePersian;

    @Transient
    private PersianDate endDatePersian;

    private Boolean isAutoRenew;  // Indicates if the subscription auto-renews
    private LocalDateTime cancelledAt;  // Timestamp when the subscription was cancelled
    private String cancellationReason;  // Reason for subscription cancellation
    private String lastUpdatedBy;  // Tracks who last updated the subscription
    private String createdBy;  // Tracks who created the subscription

    public PersianDate getStartDatePersian() {
        return startDate != null ? PersianDate.fromGregorian(startDate) : null;
    }

    public void setStartDatePersian(PersianDate persianDate) {
        this.startDate = persianDate != null ? persianDate.toGregorian() : null;
    }

    public PersianDate getEndDatePersian() {
        return endDate != null ? PersianDate.fromGregorian(endDate) : null;
    }

    public void setEndDatePersian(PersianDate persianDate) {
        this.endDate = persianDate != null ? persianDate.toGregorian() : null;
    }
}
