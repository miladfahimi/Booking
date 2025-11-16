package com.tennistime.reservation.application.scheduler;

import com.tennistime.reservation.application.service.ReservationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Periodically releases pending reservations whose payment window elapsed.
 */
@Component
public class PendingReservationReleaseJob {

    private final ReservationService reservationService;

    public PendingReservationReleaseJob(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Triggers the release of pending reservations that exceeded the allowed payment window.
     */
    @Scheduled(fixedDelayString = "${reservation.pending-release-interval-ms:60000}")
    public void releaseExpiredReservations() {
        reservationService.releaseExpiredPendingReservations();
    }
}
