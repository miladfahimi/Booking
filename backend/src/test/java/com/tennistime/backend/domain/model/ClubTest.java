package com.tennistime.backend.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ClubTest {

    private Club club;

    @BeforeEach
    void setUp() {
        club = new Club(1L, "Tennis Club 1", "123 Main St", "1234567890", "club1@example.com", "Best club in town", Collections.emptyList(), Collections.emptyList());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, club.getId());
        assertEquals("Tennis Club 1", club.getName());
        assertEquals("123 Main St", club.getAddress());
        assertEquals("1234567890", club.getPhone());
        assertEquals("club1@example.com", club.getEmail());
        assertEquals("Best club in town", club.getDescription());
        assertNotNull(club.getCourts());
        assertNotNull(club.getFeedbacks());
    }
}
