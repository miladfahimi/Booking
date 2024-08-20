package com.tennistime.bff.application.controller;

import com.tennistime.bff.application.dto.AggregatedReservationDTO;
import com.tennistime.bff.application.dto.ProviderDTO;
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

    @GetMapping("/reservations")
    public List<ReservationDTO> getAllReservations() {
        return reservationAggregationService.getAllReservations();
    }

    @GetMapping("/reservations/{reservationId}/details")
    public ResponseEntity<AggregatedReservationDTO> getAggregatedReservationDetails(@PathVariable UUID reservationId) {
        AggregatedReservationDTO aggregatedReservation = reservationAggregationService.getAggregatedReservation(reservationId);
        return ResponseEntity.ok(aggregatedReservation);
    }

    @GetMapping("/reservations/details")
    public ResponseEntity<List<AggregatedReservationDTO>> getAllAggregatedReservations() {
        List<AggregatedReservationDTO> aggregatedReservations = reservationAggregationService.getAllAggregatedReservations();
        return ResponseEntity.ok(aggregatedReservations);
    }

    /**
     * New Endpoint: Fetches providers with their associated services.
     *
     * @return a list of ProviderDTO objects
     */
    @GetMapping("/providers-with-services")
    public ResponseEntity<List<ProviderDTO>> getProvidersWithServices() {
        List<ProviderDTO> providersWithServices = reservationAggregationService.getProvidersWithServices();
        return ResponseEntity.ok(providersWithServices);
    }
}
