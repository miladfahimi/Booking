package com.tennistime.backend.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation(1L, LocalDate.of(2024, 7, 4), LocalTime.of(20, 0), LocalTime.of(22, 0), "confirmed", null, null);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, reservation.getId());
        assertEquals(LocalDate.of(2024, 7, 4), reservation.getReservationDate());
        assertEquals(LocalTime.of(20, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(22, 0), reservation.getEndTime());
        assertEquals("confirmed", reservation.getStatus());
        assertNull(reservation.getUser());
        assertNull(reservation.getCourt());
    }
}
