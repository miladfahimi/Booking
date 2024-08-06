package com.tennistime.backend.domain.model;

import com.github.mfathi91.time.PersianDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_subscription")
public class UserSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_profile_id", nullable = false)
    private Long userProfileId;

    private String subscriptionPlan;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    @Transient
    private PersianDate startDatePersian;

    @Transient
    private PersianDate endDatePersian;

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
