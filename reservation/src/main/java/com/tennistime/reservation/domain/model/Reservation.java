package com.tennistime.reservation.domain.model;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.reservation.domain.model.types.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID userId;
    private UUID providerId;
    private UUID serviceId;
    private UUID paymentId;

    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    private LocalDateTime bookedAt = LocalDateTime.now();
    private LocalDateTime expirationDate;
    private Boolean reminderEnabled = false;
    private LocalDateTime reminderSentAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING;
    private String paymentStatus;

    @Column(name = "slot_id")
    private String slotId;

    private String externalId;
    private String thirdPartyBookingId;

    private String createdBy;
    private String updatedBy;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Transient
    private PersianDate reservationDatePersian;

    public PersianDate getReservationDatePersian() {
        return PersianDate.fromGregorian(this.reservationDate);
    }

    public void setReservationDatePersian(PersianDate persianDate) {
        this.reservationDate = persianDate.toGregorian();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
