package com.tennistime.bff.application.controller;

import com.tennistime.bff.application.dto.*;
import com.tennistime.bff.application.dto.ReservationBasketItemDTO;
import com.tennistime.bff.application.service.ReservationAggregationService;
import com.tennistime.bff.application.service.ReservationBasketApplicationService;
import com.tennistime.bff.domain.model.types.ReservationStatus;
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
    private final ReservationBasketApplicationService reservationBasketApplicationService;

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
     * Creates a reservation using the reservation aggregation service.
     *
     * @param reservationDTO the reservation details provided by the client
     * @return a ResponseEntity containing the created reservation
     */
    @PostMapping("/reservations")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationAggregationService.createReservation(reservationDTO);
        return ResponseEntity.ok(createdReservation);
    }

    /**
     * Creates multiple reservations in bulk.
     *
     * @param reservationDTOs the reservations provided by the client
     * @return a ResponseEntity containing the created reservations
     */
    @PostMapping("/reservations/bulk")
    public ResponseEntity<List<ReservationDTO>> createReservations(@RequestBody List<ReservationDTO> reservationDTOs) {
        List<ReservationDTO> createdReservations = reservationAggregationService.createReservations(reservationDTOs);
        return ResponseEntity.ok(createdReservations);
    }

    /**
     * Updates the status of a reservation.
     *
     * @param reservationId the reservation identifier to update
     * @param status the new status to apply
     * @return a ResponseEntity containing the updated reservation
     */
    @PutMapping("/reservations/{reservationId}/status")
    public ResponseEntity<ReservationDTO> updateReservationStatus(
            @PathVariable UUID reservationId,
            @RequestParam ReservationStatus status
    ) {
        ReservationDTO updatedReservation = reservationAggregationService.updateReservationStatus(reservationId, status);
        return ResponseEntity.ok(updatedReservation);
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
    public ResponseEntity<ServiceDTO> getServiceSlotsWithReservations(
            @PathVariable UUID serviceId,
            @PathVariable LocalDate date) {
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

    /**
     * Retrieves the basket content for a specific user.
     *
     * @param userId identifier of the user
     * @return collection of basket items
     */
    @GetMapping("/basket/{userId}")
    public ResponseEntity<List<ReservationBasketItemDTO>> getBasket(@PathVariable UUID userId) {
        List<ReservationBasketItemDTO> basketItems = reservationBasketApplicationService.getBasket(userId);
        return ResponseEntity.ok(basketItems);
    }

    /**
     * Adds a new item to the basket or updates an existing entry.
     *
     * @param basketItemDTO basket payload
     * @return persisted basket item
     */
    @PostMapping("/basket")
    public ResponseEntity<ReservationBasketItemDTO> addBasketItem(@RequestBody ReservationBasketItemDTO basketItemDTO) {
        ReservationBasketItemDTO savedItem = reservationBasketApplicationService.addBasketItem(basketItemDTO);
        return ResponseEntity.ok(savedItem);
    }

    /**
     * Deletes a specific slot from the user's basket.
     *
     * @param userId identifier of the user
     * @param slotId identifier of the slot
     * @return empty response on success
     */
    @DeleteMapping("/basket/{userId}/{slotId}")
    public ResponseEntity<Void> removeBasketItem(@PathVariable UUID userId, @PathVariable String slotId) {
        reservationBasketApplicationService.removeBasketItem(userId, slotId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Clears all entries from the basket.
     *
     * @param userId identifier of the user
     * @return empty response on success
     */
    @DeleteMapping("/basket/{userId}")
    public ResponseEntity<Void> clearBasket(@PathVariable UUID userId) {
        reservationBasketApplicationService.clearBasket(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the status of a basket entry.
     *
     * @param userId identifier of the user
     * @param slotId identifier of the slot
     * @param status new status to set
     * @return updated basket item
     */
    @PutMapping("/basket/{userId}/{slotId}/status")
    public ResponseEntity<ReservationBasketItemDTO> updateBasketStatus(
            @PathVariable UUID userId,
            @PathVariable String slotId,
            @RequestParam ReservationStatus status) {
        ReservationBasketItemDTO updatedItem = reservationBasketApplicationService.updateBasketStatus(userId, slotId, status);
        return ResponseEntity.ok(updatedItem);
    }

}