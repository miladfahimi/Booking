package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing reservations.
 */
@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservation Management", description = "Operations related to reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID", description = "Retrieve a specific reservation by its ID.")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        ReservationDTO reservation = reservationService.findById(id);
        return reservation != null ? ResponseEntity.ok(reservation) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new reservation", description = "Allows users to create a new reservation.")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.save(reservationDTO);
        return ResponseEntity.ok(createdReservation);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a reservation by ID", description = "Allows users to update an existing reservation.")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO updatedReservation = reservationService.updateReservation(id, reservationDTO);
        return updatedReservation != null ? ResponseEntity.ok(updatedReservation) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel a reservation", description = "Allows users to cancel a reservation, changing its status to 'cancelled'.")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable Long id) {
        ReservationDTO canceledReservation = reservationService.updateReservationStatus(id, "cancelled");
        return canceledReservation != null ? ResponseEntity.ok(canceledReservation) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update reservation status", description = "Allows administrators to update the status of a reservation (e.g., confirmed, completed).")
    public ResponseEntity<ReservationDTO> updateReservationStatus(@PathVariable Long id, @RequestParam String status) {
        ReservationDTO updatedReservation = reservationService.updateReservationStatus(id, status);
        return updatedReservation != null ? ResponseEntity.ok(updatedReservation) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a reservation by ID", description = "Allows administrators to delete a reservation.")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userProfileId}")
    @Operation(summary = "Find reservations by user profile ID", description = "Retrieves all reservations for a specific user profile.")
    public ResponseEntity<List<ReservationDTO>> findReservationsByUserProfileId(@PathVariable Long userProfileId) {
        List<ReservationDTO> reservations = reservationService.findReservationsByUserProfileId(userProfileId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/court/{courtId}")
    @Operation(summary = "Find reservations by court ID", description = "Retrieves all reservations for a specific court.")
    public ResponseEntity<List<ReservationDTO>> findReservationsByCourtId(@PathVariable Long courtId) {
        List<ReservationDTO> reservations = reservationService.findReservationsByCourtId(courtId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Find reservations by date", description = "Retrieves all reservations for a specific date.")
    public ResponseEntity<List<ReservationDTO>> findReservationsByDate(@PathVariable String date) {
        List<ReservationDTO> reservations = reservationService.findReservationsByPersianDate(date);
        return ResponseEntity.ok(reservations);
    }
}
