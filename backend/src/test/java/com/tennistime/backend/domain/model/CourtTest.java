package com.tennistime.backend.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CourtTest {

    private Court court;

    @BeforeEach
    void setUp() {
        court = new Court(1L, "Court 1", "Clay", true, null, Collections.emptyList(), Collections.emptyList());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, court.getId());
        assertEquals("Court 1", court.getName());
        assertEquals("Clay", court.getType());
        assertTrue(court.isAvailability());
        assertNull(court.getClub());
        assertNotNull(court.getFeedbacks());
        assertNotNull(court.getReservations());
    }
}
