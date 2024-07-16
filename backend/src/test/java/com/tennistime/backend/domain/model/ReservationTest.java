package com.tennistime.backend.domain.model;

import com.github.mfathi91.time.PersianDate;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReservationTest {

    @Test
    public void testReservationEntity() {
        // Create and set values for AppUser
        AppUser user = new AppUser();
        user.setId(3L);
        user.setUsername("user");
        user.setPassword("password");

        // Create and set values for Court
        Court court = new Court();
        court.setId(1L);
        court.setName("Court 1");

        // Create and set values for Reservation
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setReservationDate(LocalDate.of(2024, 8, 11));
        reservation.setStartTime(LocalTime.of(10, 0));
        reservation.setEndTime(LocalTime.of(12, 0));
        reservation.setStatus("confirmed");
        reservation.setUser(user);
        reservation.setCourt(court);

        // Assert Reservation fields
        assertEquals(1L, reservation.getId());
        assertEquals(LocalDate.of(2024, 8, 11), reservation.getReservationDate());
        assertEquals(LocalTime.of(10, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(12, 0), reservation.getEndTime());
        assertEquals("confirmed", reservation.getStatus());
        assertEquals(3L, reservation.getUser().getId());
        assertEquals("user", reservation.getUser().getUsername());
        assertEquals(1L, reservation.getCourt().getId());
        assertEquals("Court 1", reservation.getCourt().getName());

        // Set and assert Persian date
        PersianDate persianDate = PersianDate.fromGregorian(LocalDate.of(2024, 8, 11));
        reservation.setReservationDatePersian(persianDate);
        assertNotNull(reservation.getReservationDatePersian());
        assertEquals(persianDate, reservation.getReservationDatePersian());

        // Ensure the Gregorian date is updated correctly
        assertEquals(LocalDate.of(2024, 8, 11), reservation.getReservationDate());
    }
}
