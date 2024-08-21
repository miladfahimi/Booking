package com.tennistime.bff.application.controller;

import com.tennistime.bff.application.dto.AggregatedReservationDTO;
import com.tennistime.bff.application.dto.ProviderDTO;
import com.tennistime.bff.application.dto.ReservationDTO;
import com.tennistime.bff.application.service.ReservationAggregationService;
import com.tennistime.bff.exceptions.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for handling user portal-related requests.
 * This includes operations like fetching reservations, providers with services, and calculating service slots.
 */
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

    /**
     * Gets the updated availability slots for a service on a specific date.
     *
     * @param serviceId the UUID of the service
     * @param date      the date for which the slots should be checked
     * @return a ResponseEntity containing the list of updated slot statuses
     */
    @GetMapping("/services/{serviceId}/slots/{date}")
    public ResponseEntity<List<String>> getServiceSlotsWithReservations(@PathVariable UUID serviceId, @PathVariable LocalDate date) {
        List<String> slots = reservationAggregationService.calculateAndUpdateSlots(serviceId, date);
        return ResponseEntity.ok(slots);
    }
}
