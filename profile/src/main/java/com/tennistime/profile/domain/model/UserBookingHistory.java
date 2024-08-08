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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID userId;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    private String status;

    @Transient
    private PersianDate bookingDatePersian;

    public PersianDate getBookingDatePersian() {
        return PersianDate.fromGregorian(this.bookingDate.toLocalDate());
    }

    public void setBookingDatePersian(PersianDate persianDate) {
        this.bookingDate = persianDate.toGregorian().atStartOfDay();
    }

}
