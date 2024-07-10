package com.tennistime.backend.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "2024-07-04")
    private LocalDate reservationDate;

    @Schema(example = "20:00")
    private LocalTime startTime;

    @Schema(example = "22:00")
    private LocalTime endTime;

    @Schema(example = "confirmed")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "court_id")
    private Court court;
}
