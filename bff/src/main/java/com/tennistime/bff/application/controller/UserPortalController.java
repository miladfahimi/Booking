package com.tennistime.bff.application.controller;

import com.tennistime.bff.application.dto.AggregatedReservationDTO;
import com.tennistime.bff.application.dto.ReservationDTO;
import com.tennistime.bff.application.service.ReservationAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/portal/user")
@RequiredArgsConstructor
public class UserPortalController {

    private final ReservationAggregationService reservationAggregationService;

    /**
     * Fetches a list of all reservations.
     *
     * @return a list of ReservationDTO objects
     */
    @GetMapping("/reservations")
    public List<ReservationDTO> getAllReservations() {
        return reservationAggregationService.getAllReservations();
    }

    /**
     * Fetches details of a single reservation, aggregated with related entities.
     *
     * @param reservationId the ID of the reservation
     * @return the aggregated reservation details
     */
    @GetMapping("/reservations/{reservationId}/details")
    public ResponseEntity<AggregatedReservationDTO> getAggregatedReservationDetails(@PathVariable UUID reservationId) {
        AggregatedReservationDTO aggregatedReservation = reservationAggregationService.getAggregatedReservation(reservationId);
        return ResponseEntity.ok(aggregatedReservation);
    }

    /**
     * Fetches a list of all reservations with full details (aggregated data).
     *
     * @return a list of AggregatedReservationDTO objects
     */
    @GetMapping("/reservations/details")
    public ResponseEntity<List<AggregatedReservationDTO>> getAllAggregatedReservations() {
        List<AggregatedReservationDTO> aggregatedReservations = reservationAggregationService.getAllAggregatedReservations();
        return ResponseEntity.ok(aggregatedReservations);
    }
}
