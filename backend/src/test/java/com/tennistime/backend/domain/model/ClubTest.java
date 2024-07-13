package com.tennistime.backend.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClubTest {

    private Club club;
    private Court court;

    @BeforeEach
    void setUp() {
        club = new Club(1L, "Test Club", "123 Main St", "1234567890", "test@club.com", "A great club", new ArrayList<>(), new ArrayList<>());
        court = new Court(1L, "Court 1", "Clay", true, club, new ArrayList<>(), new ArrayList<>());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, club.getId());
        assertEquals("Test Club", club.getName());
        assertEquals("123 Main St", club.getAddress());
        assertEquals("1234567890", club.getPhone());
        assertEquals("test@club.com", club.getEmail());
        assertEquals("A great club", club.getDescription());
        assertNotNull(club.getCourts());
    }

    @Test
    void addCourt() {
        club.addCourt(court);
        assertEquals(1, club.getCourts().size());
        assertEquals(court, club.getCourts().get(0));
        assertEquals(club, court.getClub());
    }
}
