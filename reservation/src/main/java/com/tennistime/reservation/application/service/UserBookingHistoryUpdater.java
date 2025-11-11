package com.tennistime.reservation.application.service;

import com.tennistime.reservation.application.dto.UserBookingHistoryUpdateRequest;
import com.tennistime.reservation.domain.model.Reservation;
import com.tennistime.reservation.infrastructure.feign.UserBookingHistoryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Coordinates synchronization of reservation updates with the profile service booking history endpoint.
 */
@Component
public class UserBookingHistoryUpdater {

    private static final Logger logger = LoggerFactory.getLogger(UserBookingHistoryUpdater.class);

    private final UserBookingHistoryClient userBookingHistoryClient;

    @Autowired
    public UserBookingHistoryUpdater(UserBookingHistoryClient userBookingHistoryClient) {
        this.userBookingHistoryClient = userBookingHistoryClient;
    }

    /**
     * Updates the booking history record for the provided reservation using the profile service client.
     *
     * @param reservation reservation that triggered the synchronization.
     */
    public void updateUserBookingHistory(Reservation reservation) {
        String bookingDate = reservation.getReservationDate().atTime(reservation.getStartTime()).toString();
        UserBookingHistoryUpdateRequest updateRequest = new UserBookingHistoryUpdateRequest(
                reservation.getUserId(),
                reservation.getId(),
                reservation.getServiceId(),
                bookingDate,
                reservation.getStatus(),
                reservation.getReservationDatePersian().toString(),
                false,
                false,
                null
        );

        try {
            logger.info("Updating user booking history: {}", updateRequest);
            userBookingHistoryClient.updateUserBookingHistory(updateRequest);
            logger.info("Successfully updated UserBookingHistory for reservation ID: {}", reservation.getId());
        } catch (Exception e) {
            logger.error("Failed to update UserBookingHistory for reservation ID: {} - Error: {}", reservation.getId(), e.getMessage());
        }
    }
}