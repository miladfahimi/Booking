package com.tennistime.reservation.application.controller;

import com.tennistime.reservation.application.dto.ReservationBasketItemDTO;
import com.tennistime.reservation.application.service.ReservationBasketService;
import com.tennistime.reservation.domain.model.types.ReservationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller exposing CRUD-style basket endpoints.
 */
@RestController
@RequestMapping("/reservations/basket")
@RequiredArgsConstructor
@Tag(name = "Reservation Basket", description = "Manage basket items for reservation flows")
public class ReservationBasketController {

    private final ReservationBasketService reservationBasketService;

    /**
     * Lists all basket items belonging to the provided user identifier.
     *
     * @param userId owner of the basket
     * @return collection of basket entries
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Retrieve basket contents", description = "Fetch all basket items for the given user")
    public ResponseEntity<List<ReservationBasketItemDTO>> getBasket(
            @Parameter(description = "Identifier of the user", required = true) @PathVariable UUID userId) {
        return ResponseEntity.ok(reservationBasketService.getBasket(userId));
    }

    /**
     * Adds a new entry or updates the existing one for the same user and slot.
     *
     * @param basketItemDTO basket payload
     * @return persisted basket entry
     */
    @PostMapping
    @Operation(summary = "Add basket item", description = "Store or update a basket item for the current user")
    public ResponseEntity<ReservationBasketItemDTO> addBasketItem(
            @Parameter(description = "Basket item payload", required = true) @RequestBody ReservationBasketItemDTO basketItemDTO) {
        return ResponseEntity.ok(reservationBasketService.addOrUpdateItem(basketItemDTO));
    }

    /**
     * Removes a slot from the basket.
     *
     * @param userId user identifier
     * @param slotId slot identifier
     * @return empty response once the removal is complete
     */
    @DeleteMapping("/{userId}/{slotId}")
    @Operation(summary = "Remove basket item", description = "Delete a slot from the user's basket")
    public ResponseEntity<Void> removeBasketItem(
            @Parameter(description = "Identifier of the user", required = true) @PathVariable UUID userId,
            @Parameter(description = "Identifier of the slot", required = true) @PathVariable String slotId) {
        reservationBasketService.removeItem(userId, slotId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes all basket entries for a user.
     *
     * @param userId user identifier
     * @return empty response once cleared
     */
    @DeleteMapping("/{userId}")
    @Operation(summary = "Clear basket", description = "Remove all items from the user's basket")
    public ResponseEntity<Void> clearBasket(
            @Parameter(description = "Identifier of the user", required = true) @PathVariable UUID userId) {
        reservationBasketService.clearBasket(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the status for a specific basket entry.
     *
     * @param userId user identifier
     * @param slotId slot identifier
     * @param status new status to assign
     * @return updated basket item
     */
    @PutMapping("/{userId}/{slotId}/status")
    @Operation(summary = "Update basket status", description = "Adjust the status of a basket entry")
    public ResponseEntity<ReservationBasketItemDTO> updateBasketStatus(
            @Parameter(description = "Identifier of the user", required = true) @PathVariable UUID userId,
            @Parameter(description = "Identifier of the slot", required = true) @PathVariable String slotId,
            @Parameter(description = "New status", required = true) @RequestParam ReservationStatus status) {
        return ResponseEntity.ok(reservationBasketService.updateStatus(userId, slotId, status));
    }
}
