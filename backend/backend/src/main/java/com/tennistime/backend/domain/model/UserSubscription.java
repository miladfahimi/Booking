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

    private Long userId; // Storing user ID instead of user reference

    private String subscriptionPlan;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    @Transient
    private PersianDate startDatePersian;

    @Transient
    private PersianDate endDatePersian;

    public PersianDate getStartDatePersian() {
        return PersianDate.fromGregorian(this.startDate);
    }

    public void setStartDatePersian(PersianDate persianDate) {
        this.startDate = persianDate.toGregorian();
    }

    public PersianDate getEndDatePersian() {
        return PersianDate.fromGregorian(this.endDate);
    }

    public void setEndDatePersian(PersianDate persianDate) {
        this.endDate = persianDate.toGregorian();
    }
}
