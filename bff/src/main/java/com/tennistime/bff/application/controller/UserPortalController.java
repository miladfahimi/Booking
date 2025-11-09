package com.tennistime.bff.application.controller;

import com.tennistime.bff.application.dto.AggregatedReservationDTO;
import com.tennistime.bff.application.dto.ProviderDTO;
import com.tennistime.bff.application.dto.ReservationDTO;
import com.tennistime.bff.application.dto.SlotDTO;
import com.tennistime.bff.application.dto.ServiceDTO;
import com.tennistime.bff.application.service.ReservationAggregationService;
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

    /**
     * Retrieves all reservations.
     *
     * @return a list of ReservationDTO objects
     */
    @GetMapping("/reservations")
    public List<ReservationDTO> getAllReservations() {
        return reservationAggregationService.getAllReservations();
    }

    /**
     * Retrieves aggregated details for a specific reservation.
     *
     * @param reservationId the UUID of the reservation
     * @return a ResponseEntity containing the aggregated reservation details
     */
    @GetMapping("/reservations/{reservationId}/details")
    public ResponseEntity<AggregatedReservationDTO> getAggregatedReservationDetails(@PathVariable UUID reservationId) {
        AggregatedReservationDTO aggregatedReservation = reservationAggregationService.getAggregatedReservation(reservationId);
        return ResponseEntity.ok(aggregatedReservation);
    }

    /**
     * Retrieves aggregated details for all reservations.
     *
     * @return a ResponseEntity containing a list of aggregated reservation details
     */
    @GetMapping("/reservations/details")
    public ResponseEntity<List<AggregatedReservationDTO>> getAllAggregatedReservations() {
        List<AggregatedReservationDTO> aggregatedReservations = reservationAggregationService.getAllAggregatedReservations();
        return ResponseEntity.ok(aggregatedReservations);
    }

    /**
     * Fetches providers with their associated services.
     *
     * @return a ResponseEntity containing a list of ProviderDTO objects
     */
    @GetMapping("/providers-with-services")
    public ResponseEntity<List<ProviderDTO>> getProvidersWithServices() {
        List<ProviderDTO> providersWithServices = reservationAggregationService.getProvidersWithServices();
        return ResponseEntity.ok(providersWithServices);
    }

    /**
     * Calculates available slots for a service.
     *
     * @param serviceId the UUID of the service
     * @return a ResponseEntity containing a list of SlotDTO objects
     */
    @GetMapping("/services/{serviceId}/slots")
    public ResponseEntity<List<SlotDTO>> getServiceSlots(@PathVariable UUID serviceId) {
        ServiceDTO serviceWithSlots = reservationAggregationService.calculateAndUpdateSlots(serviceId, LocalDate.now());
        return ResponseEntity.ok(serviceWithSlots.getSlots());
    }

    /**
     * Gets the updated availability slots for a service on a specific date.
     *
     * @param serviceId the UUID of the service
     * @param date      the date for which the slots should be checked
     * @return a ResponseEntity containing the updated ServiceDTO with slots information
     */
    @GetMapping("/services/{serviceId}/slots/{date}")
    public ResponseEntity<ServiceDTO> getServiceSlotsWithReservations(@PathVariable UUID serviceId, @PathVariable LocalDate date) {
        ServiceDTO serviceWithSlots = reservationAggregationService.calculateAndUpdateSlots(serviceId, date);
        return ResponseEntity.ok(serviceWithSlots);
    }

    /**
     * Retrieves slots for all services filtered by type and date.
     *
     * @param type the service type to filter by; if null, blank, "all", "null", or "undefined", all types are included
     * @param date the date for which slot availability should be calculated
     * @return a ResponseEntity containing the list of services with their corresponding slots
     */
    // @GetMapping({"/services/{type}/slots/{date}", "/services/slots/{date}"})  ---->>>>> this does not work as it same shape as the other endpoint
    @GetMapping({"/services/slots/{date}"})
    public ResponseEntity<List<ServiceDTO>> getServiceSlotsByTypeAndDate(
            @PathVariable(value = "type", required = false) String type,
            @PathVariable LocalDate date) {
        List<ServiceDTO> servicesWithSlots = reservationAggregationService.getServiceSlotsByTypeAndDate(type, date);
        return ResponseEntity.ok(servicesWithSlots);
    }

}