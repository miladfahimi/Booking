package com.tennistime.reservation.domain.model;

import com.github.mfathi91.time.PersianDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // Updated from UUID to UUID

    private UUID userId;

    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    private String status;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Transient
    private PersianDate reservationDatePersian;

    public PersianDate getReservationDatePersian() {
        return PersianDate.fromGregorian(this.reservationDate);
    }

    public void setReservationDatePersian(PersianDate persianDate) {
        this.reservationDate = persianDate.toGregorian();
    }
}
