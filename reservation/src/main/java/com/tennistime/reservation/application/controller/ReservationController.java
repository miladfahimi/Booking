package com.tennistime.reservation.application.controller;

import com.tennistime.reservation.application.dto.ReservationDTO;
import com.tennistime.reservation.application.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for handling reservation-related requests.
 */
@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservations", description = "API for managing reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    @Operation(summary = "Get all reservations", description = "Retrieve a list of all reservations")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.findAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID", description = "Retrieve a reservation by its ID")
    public ResponseEntity<ReservationDTO> getReservationById(
            @Parameter(description = "ID of the reservation to retrieve", required = true) @PathVariable UUID id) { // Updated from UUID to UUID
        ReservationDTO reservation = reservationService.findById(id);
        return reservation != null ? ResponseEntity.ok(reservation) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new reservation", description = "Create a new reservation with the provided details")
    public ResponseEntity<ReservationDTO> createReservation(
            @Parameter(description = "Details of the reservation to create", required = true) @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.save(reservationDTO);
        return ResponseEntity.ok(createdReservation);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a reservation", description = "Update an existing reservation with the provided details")
    public ResponseEntity<ReservationDTO> updateReservation(
            @Parameter(description = "ID of the reservation to update", required = true) @PathVariable UUID id, // Updated from UUID to UUID
            @Parameter(description = "Updated details of the reservation", required = true) @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO updatedReservation = reservationService.updateReservation(id, reservationDTO);
        return updatedReservation != null ? ResponseEntity.ok(updatedReservation) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update reservation status", description = "Update the status of an existing reservation")
    public ResponseEntity<ReservationDTO> updateReservationStatus(
            @Parameter(description = "ID of the reservation to update the status", required = true) @PathVariable UUID id, // Updated from UUID to UUID
            @Parameter(description = "New status of the reservation", required = true) @RequestParam String status) {
        ReservationDTO updatedReservation = reservationService.updateReservationStatus(id, status);
        return updatedReservation != null ? ResponseEntity.ok(updatedReservation) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a reservation", description = "Delete a reservation by its ID")
    public ResponseEntity<Void> deleteReservation(
            @Parameter(description = "ID of the reservation to delete", required = true) @PathVariable UUID id) { // Updated from UUID to UUID
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get reservations by user ID", description = "Retrieve all reservations made by a specific user")
    public ResponseEntity<List<ReservationDTO>> getReservationsByUserId(
            @Parameter(description = "ID of the user whose reservations to retrieve", required = true) @PathVariable UUID userId) {
        List<ReservationDTO> reservations = reservationService.findReservationsByUserId(userId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/service/{serviceId}")
    @Operation(summary = "Get reservations by service ID", description = "Retrieve all reservations for a specific service")
    public ResponseEntity<List<ReservationDTO>> getReservationsByServiceId(
            @Parameter(description = "ID of the service whose reservations to retrieve", required = true) @PathVariable UUID serviceId) {
        List<ReservationDTO> reservations = reservationService.findReservationsByServiceId(serviceId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/date/{persianDate}")
    @Operation(summary = "Get reservations by Persian date", description = "Retrieve all reservations for a specific Persian date")
    public ResponseEntity<List<ReservationDTO>> getReservationsByPersianDate(
            @Parameter(description = "Persian date to retrieve reservations for", required = true) @PathVariable String persianDate) {
        List<ReservationDTO> reservations = reservationService.findReservationsByPersianDate(persianDate);
        return ResponseEntity.ok(reservations);
    }
}
