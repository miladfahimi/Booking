package com.tennistime.backend.domain.model;

import com.github.mfathi91.time.PersianDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(nullable = false)
    private Long userId;

    private String status;

    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private AppUser user;

    @ManyToOne
    @JoinColumn(name = "court_id")
    private Court court;

    // This field will be mapped in DTO
    @Transient
    private PersianDate reservationDatePersian;

    public PersianDate getReservationDatePersian() {
        return PersianDate.fromGregorian(this.reservationDate);
    }

    public void setReservationDatePersian(PersianDate persianDate) {
        this.reservationDate = persianDate.toGregorian();
    }
}
