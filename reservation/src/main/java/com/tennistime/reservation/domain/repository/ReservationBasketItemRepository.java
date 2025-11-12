package com.tennistime.reservation.domain.repository;

import com.tennistime.reservation.domain.model.basket.ReservationBasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for accessing {@link ReservationBasketItem} aggregates.
 */
public interface ReservationBasketItemRepository extends JpaRepository<ReservationBasketItem, UUID> {

    /**
     * Retrieves every basket item for the given user.
     *
     * @param userId identifier of the customer
     * @return collection of basket entries
     */
    List<ReservationBasketItem> findByUserId(UUID userId);

    /**
     * Locates a basket entry by its slot identifier within a user's basket.
     *
     * @param userId identifier of the customer
     * @param slotId identifier of the slot
     * @return optional basket entry
     */
    Optional<ReservationBasketItem> findByUserIdAndSlotId(UUID userId, String slotId);

    /**
     * Deletes a specific slot from a user's basket.
     *
     * @param userId identifier of the customer
     * @param slotId identifier of the slot
     */
    void deleteByUserIdAndSlotId(UUID userId, String slotId);

    /**
     * Deletes all entries for the provided user identifier.
     *
     * @param userId identifier of the customer
     */
    void deleteByUserId(UUID userId);
}
