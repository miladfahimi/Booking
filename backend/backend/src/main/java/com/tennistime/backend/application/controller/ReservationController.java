package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservation Management", description = "Operations related to reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    @Operation(summary = "Get all reservations")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.findAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        ReservationDTO reservation = reservationService.findById(id);
        return reservation != null ? ResponseEntity.ok(reservation) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new reservation")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.save(reservationDTO);
        return ResponseEntity.ok(createdReservation);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a reservation by ID")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Find reservations by user ID")
    public ResponseEntity<List<ReservationDTO>> findReservationsByUserId(@PathVariable Long userId) {
        List<ReservationDTO> reservations = reservationService.findReservationsByUserId(userId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/court/{courtId}")
    @Operation(summary = "Find reservations by court ID")
    public ResponseEntity<List<ReservationDTO>> findReservationsByCourtId(@PathVariable Long courtId) {
        List<ReservationDTO> reservations = reservationService.findReservationsByCourtId(courtId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/persian/{date}")
    @Operation(summary = "Find reservations by Persian date")
    public ResponseEntity<List<ReservationDTO>> findReservationsByPersianDate(@PathVariable String date) {
        List<ReservationDTO> reservations = reservationService.findReservationsByPersianDate(date);
        return ResponseEntity.ok(reservations);
    }
}
