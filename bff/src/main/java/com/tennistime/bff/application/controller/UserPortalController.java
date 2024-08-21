package com.tennistime.bff.application.controller;

import com.tennistime.bff.application.dto.AggregatedReservationDTO;
import com.tennistime.bff.application.dto.ProviderDTO;
import com.tennistime.bff.application.dto.ReservationDTO;
import com.tennistime.bff.application.service.ReservationAggregationService;
import com.tennistime.bff.exceptions.ServiceNotFoundException;
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
     * Fetches providers with their associated services.
     *
     * @return a list of ProviderDTO objects
     */
    @GetMapping("/providers-with-services")
    public ResponseEntity<List<ProviderDTO>> getProvidersWithServices() {
        List<ProviderDTO> providersWithServices = reservationAggregationService.getProvidersWithServices();
        return ResponseEntity.ok(providersWithServices);
    }

    /**
     * Calculates available slots for a service.
     *
     * @param serviceId the ID of the service
     * @return a list of slot statuses as strings
     * @throws ServiceNotFoundException if the service is not found
     */
    @GetMapping("/services/{serviceId}/slots")
    public ResponseEntity<List<String>> getServiceSlots(@PathVariable UUID serviceId) {
        List<String> slots = reservationAggregationService.calculateSlots(serviceId);
        return ResponseEntity.ok(slots);
    }
}
