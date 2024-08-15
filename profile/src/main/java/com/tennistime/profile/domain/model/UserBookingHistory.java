package com.tennistime.profile.domain.model;

import com.github.mfathi91.time.PersianDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_booking_history")
public class UserBookingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID reservationId;

    @Column(nullable = false)
    private UUID serviceId;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    private String status;

    private boolean isItArchived = false;

    private boolean isUserNotified = false;

    private String notes;

    @Transient
    private PersianDate bookingDatePersian;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public PersianDate getBookingDatePersian() {
        return PersianDate.fromGregorian(this.bookingDate.toLocalDate());
    }

    public void setBookingDatePersian(PersianDate persianDate) {
        if (persianDate != null) {
            this.bookingDate = persianDate.toGregorian().atStartOfDay();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
