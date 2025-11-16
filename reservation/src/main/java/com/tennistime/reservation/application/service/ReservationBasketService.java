package com.tennistime.reservation.application.service;

import com.tennistime.reservation.application.dto.ReservationBasketItemDTO;
import com.tennistime.reservation.application.mapper.ReservationBasketItemMapper;
import com.tennistime.reservation.application.notification.SlotStatusNotifier;
import com.tennistime.reservation.domain.model.basket.ReservationBasketItem;
import com.tennistime.reservation.domain.model.types.ReservationStatus;
import com.tennistime.reservation.domain.repository.ReservationBasketItemRepository;
import com.tennistime.reservation.domain.notification.SlotStatusNotification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Application service exposing reservation basket operations.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationBasketService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationBasketService.class);

    private final ReservationBasketItemRepository basketItemRepository;
    private final ReservationBasketItemMapper basketItemMapper;
    private final SlotStatusNotifier slotStatusNotifier;

    /**
     * Retrieves the current basket items for a user.
     *
     * @param userId identifier of the customer
     * @return basket items mapped to DTOs
     */
    public List<ReservationBasketItemDTO> getBasket(UUID userId) {
        logger.info("Fetching basket items for user {}", userId);
        return basketItemRepository.findByUserId(userId).stream()
                .map(basketItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Adds or replaces a basket entry for the given user and slot.
     *
     * @param basketItemDTO payload describing the basket item
     * @return persisted representation
     */
    @Transactional
    public ReservationBasketItemDTO addOrUpdateItem(ReservationBasketItemDTO basketItemDTO) {
        Objects.requireNonNull(basketItemDTO.getUserId(), "User identifier is required");
        Objects.requireNonNull(basketItemDTO.getSlotId(), "Slot identifier is required");
        logger.info("Storing basket item for user {} and slot {}", basketItemDTO.getUserId(), basketItemDTO.getSlotId());
        ReservationBasketItem basketItem = basketItemRepository.findByUserIdAndSlotId(
                        basketItemDTO.getUserId(),
                        basketItemDTO.getSlotId())
                .orElseGet(() -> basketItemMapper.toEntity(basketItemDTO));

        if (basketItem.getStatus() == null) {
            basketItem.setStatus(ReservationStatus.IN_BASKET);
        }

        basketItemMapper.updateEntity(basketItemDTO, basketItem);
        if (basketItem.getStatus() == null) {
            basketItem.setStatus(ReservationStatus.IN_BASKET);
        }

        ReservationBasketItem persisted = basketItemRepository.save(basketItem);
        notifyStatusChange(persisted, persisted.getStatus());
        return basketItemMapper.toDTO(persisted);
    }

    /**
     * Removes a specific slot from the user's basket.
     *
     * @param userId identifier of the customer
     * @param slotId identifier of the slot
     */
    @Transactional
    public void removeItem(UUID userId, String slotId) {
        Objects.requireNonNull(slotId, "Slot identifier is required");
        logger.info("Removing slot {} from user {} basket", slotId, userId);
        ReservationBasketItem basketItem = basketItemRepository.findByUserIdAndSlotId(userId, slotId)
                .orElse(null);
        basketItemRepository.deleteByUserIdAndSlotId(userId, slotId);
        if (basketItem != null) {
            notifyStatusChange(basketItem, ReservationStatus.AVAILABLE);
        }
    }

    /**
     * Clears every basket item for the provided user.
     *
     * @param userId identifier of the customer
     */
    @Transactional
    public void clearBasket(UUID userId) {
        logger.info("Clearing basket for user {}", userId);
        List<ReservationBasketItem> items = basketItemRepository.findByUserId(userId);
        basketItemRepository.deleteByUserId(userId);
        items.forEach(item -> notifyStatusChange(item, ReservationStatus.AVAILABLE));
    }

    /**
     * Updates the status of a specific basket entry.
     *
     * @param userId identifier of the customer
     * @param slotId identifier of the slot
     * @param status new status value
     * @return updated basket item
     */
    @Transactional
    public ReservationBasketItemDTO updateStatus(UUID userId, String slotId, ReservationStatus status) {
        Objects.requireNonNull(status, "Status is required");
        logger.info("Updating basket status for user {} slot {} to {}", userId, slotId, status);
        ReservationBasketItem basketItem = basketItemRepository.findByUserIdAndSlotId(userId, slotId)
                .orElseThrow(() -> new IllegalArgumentException("Basket item not found for slot " + slotId));
        basketItem.setStatus(status);
        ReservationBasketItem persisted = basketItemRepository.save(basketItem);
        notifyStatusChange(persisted, persisted.getStatus());
        return basketItemMapper.toDTO(persisted);
    }

    /**
     * Publishes a real-time notification describing the provided basket item's status.
     *
     * @param basketItem basket item containing slot identifiers
     * @param status     resolved status to propagate
     */
    private void notifyStatusChange(ReservationBasketItem basketItem, ReservationStatus status) {
        SlotStatusNotification notification = SlotStatusNotification.builder()
                .slotId(extractRawSlotId(basketItem.getSlotId()))
                .compositeSlotId(basketItem.getSlotId())
                .serviceId(basketItem.getServiceId())
                .status(status)
                .reservationDate(basketItem.getReservationDate() != null
                    ? basketItem.getReservationDate().toString()
                    : null)
                .build();
        slotStatusNotifier.publish(notification);
    }

    /**
     * Extracts the underlying slot identifier from the composite key stored in the basket.
     *
     * @param compositeSlotId identifier stored in persistence
     * @return raw slot identifier without service prefix
     */
    private String extractRawSlotId(String compositeSlotId) {
        int separatorIndex = compositeSlotId.indexOf(':');
        if (separatorIndex < 0) {
            return compositeSlotId;
        }
        return compositeSlotId.substring(separatorIndex + 1);
    }
}