package com.tennistime.reservation.application.controller;

import com.tennistime.reservation.application.dto.ReservationDTO;
import com.tennistime.reservation.application.service.ReservationService;
import com.tennistime.reservation.domain.model.types.ReservationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Controller for handling reservation-related requests.
 */
@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservations", description = "API for managing reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all reservations", description = "Retrieve a list of all reservations")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.findAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID", description = "Retrieve a reservation by its ID")
    public ResponseEntity<ReservationDTO> getReservationById(
            @Parameter(description = "ID of the reservation to retrieve", required = true) @PathVariable UUID id) {
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

    /**
     * Accepts multiple reservation requests and persists them with a pending status.
     *
     * @param reservationRequests reservations to create.
     * @return response containing all created reservations.
     */
    @PostMapping("/bulk")
    @Operation(summary = "Create multiple reservations", description = "Create several reservations in a single request and mark them as pending")
    public ResponseEntity<List<ReservationDTO>> createReservations(
            @Parameter(description = "Collection of reservations to create", required = true) @RequestBody List<ReservationDTO> reservationRequests) {
        List<ReservationDTO> createdReservations = reservationService.saveAll(reservationRequests);
        return ResponseEntity.ok(createdReservations);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a reservation", description = "Update an existing reservation with the provided details")
    public ResponseEntity<ReservationDTO> updateReservation(
            @Parameter(description = "ID of the reservation to update", required = true) @PathVariable UUID id,
            @Parameter(description = "Updated details of the reservation", required = true) @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO updatedReservation = reservationService.updateReservation(id, reservationDTO);
        return updatedReservation != null ? ResponseEntity.ok(updatedReservation) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update reservation status", description = "Update the status of an existing reservation")
    public ResponseEntity<ReservationDTO> updateReservationStatus(
            @Parameter(description = "ID of the reservation to update the status", required = true) @PathVariable UUID id,
            @Parameter(description = "New status of the reservation", required = true) @RequestParam ReservationStatus status) {
        ReservationDTO updatedReservation = reservationService.updateReservationStatus(id, status);
        return updatedReservation != null ? ResponseEntity.ok(updatedReservation) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a reservation", description = "Delete a reservation by its ID")
    public ResponseEntity<Void> deleteReservation(
            @Parameter(description = "ID of the reservation to delete", required = true) @PathVariable UUID id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    @Operation(summary = "Get reservations by optional filters", description = "Retrieve reservations filtered by user ID, service ID, provider ID, status, reservation date, Persian date, start time, end time, and reference number.")
    public ResponseEntity<List<ReservationDTO>> getReservations(
            @Parameter(description = "ID of the user whose reservations to retrieve") @RequestParam(required = false) UUID userId,
            @Parameter(description = "ID of the service whose reservations to retrieve") @RequestParam(required = false) UUID serviceId,
            @Parameter(description = "ID of the provider offering the service") @RequestParam(required = false) UUID providerId,
            @Parameter(description = "Status of the reservation to retrieve") @RequestParam(required = false) ReservationStatus status,
            @Parameter(description = "Gregorian date of the reservation") @RequestParam(required = false) LocalDate reservationDate,
            @Parameter(description = "Persian date to retrieve reservations for") @RequestParam(required = false) String persianDate,
            @Parameter(description = "Start time of the reservation") @RequestParam(required = false) LocalTime startTime,
            @Parameter(description = "End time of the reservation") @RequestParam(required = false) LocalTime endTime,
            @Parameter(description = "Reference number of the reservation") @RequestParam(required = false) String referenceNumber) {

        List<ReservationDTO> reservations = reservationService.findReservations(userId, serviceId, providerId, status, reservationDate, persianDate, startTime, endTime, referenceNumber);
        return ResponseEntity.ok(reservations);
    }
}
