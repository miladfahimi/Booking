package com.tennistime.bff.application.service;

import com.tennistime.bff.application.dto.ReservationBasketItemDTO;
import com.tennistime.bff.domain.model.types.ReservationStatus;
import com.tennistime.bff.exceptions.ExternalServiceException;
import com.tennistime.bff.infrastructure.feign.ReservationServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Application service orchestrating basket calls against the reservation service.
 */
@Service
@RequiredArgsConstructor
public class ReservationBasketApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationBasketApplicationService.class);

    private final ReservationServiceClient reservationServiceClient;

    /**
     * Retrieves basket items for a user.
     *
     * @param userId identifier of the customer
     * @return list of basket entries
     */
    public List<ReservationBasketItemDTO> getBasket(UUID userId) {
        try {
            logger.info("Fetching basket for user {}", userId);
            return reservationServiceClient.getBasketItems(userId);
        } catch (FeignException e) {
            logger.error("Error while fetching basket for user {}: {}", userId, e.getMessage());
            throw new ExternalServiceException("Unable to fetch basket entries.", e);
        }
    }

    /**
     * Stores a basket item.
     *
     * @param basketItemDTO payload describing the item
     * @return persisted representation
     */
    public ReservationBasketItemDTO addBasketItem(ReservationBasketItemDTO basketItemDTO) {
        try {
            logger.info("Adding basket item for user {} slot {}", basketItemDTO.getUserId(), basketItemDTO.getSlotId());
            return reservationServiceClient.addBasketItem(basketItemDTO);
        } catch (FeignException e) {
            logger.error("Error while storing basket item for user {}: {}", basketItemDTO.getUserId(), e.getMessage());
            throw new ExternalServiceException("Unable to store basket item.", e);
        }
    }

    /**
     * Removes a slot from a basket.
     *
     * @param userId identifier of the customer
     * @param slotId identifier of the slot
     */
    public void removeBasketItem(UUID userId, String slotId) {
        try {
            logger.info("Removing slot {} from basket of user {}", slotId, userId);
            reservationServiceClient.removeBasketItem(userId, slotId);
        } catch (FeignException e) {
            logger.error("Error while removing basket item for user {}: {}", userId, e.getMessage());
            throw new ExternalServiceException("Unable to remove basket item.", e);
        }
    }

    /**
     * Clears a user's basket.
     *
     * @param userId identifier of the customer
     */
    public void clearBasket(UUID userId) {
        try {
            logger.info("Clearing basket for user {}", userId);
            reservationServiceClient.clearBasket(userId);
        } catch (FeignException e) {
            logger.error("Error while clearing basket for user {}: {}", userId, e.getMessage());
            throw new ExternalServiceException("Unable to clear basket.", e);
        }
    }

    /**
     * Updates the status of a basket item.
     *
     * @param userId identifier of the customer
     * @param slotId identifier of the slot
     * @param status new status to assign
     * @return updated basket entry
     */
    public ReservationBasketItemDTO updateBasketStatus(UUID userId, String slotId, ReservationStatus status) {
        try {
            logger.info("Updating basket status for user {} slot {} to {}", userId, slotId, status);
            return reservationServiceClient.updateBasketStatus(userId, slotId, status);
        } catch (FeignException e) {
            logger.error("Error while updating basket status for user {}: {}", userId, e.getMessage());
            throw new ExternalServiceException("Unable to update basket status.", e);
        }
    }
}
