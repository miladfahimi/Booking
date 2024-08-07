package com.tennistime.backend.domain.model.userDetails;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.model.Reservation;
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

    public Long getCourtId() {
        return court != null ? court.getId() : null;
    }

    public UserBookingHistory(Reservation reservation) {
        this.userId = reservation.getUserId();
        this.court = reservation.getCourt();
        this.bookingDate = reservation.getReservationDate().atTime(reservation.getStartTime());
        this.status = reservation.getStatus();
        this.bookingDatePersian = reservation.getReservationDatePersian();
    }
}
