package com.tennistime.backend.domain.model;

import com.github.mfathi91.time.PersianDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private Long userProfileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

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

    public UserBookingHistory(Reservation reservation) {
        this.userProfileId = reservation.getUserProfileId();
        this.court = reservation.getCourt();
        this.bookingDate = reservation.getReservationDate().atTime(reservation.getStartTime());
        this.status = reservation.getStatus();
        this.bookingDatePersian = reservation.getReservationDatePersian();
    }
}
