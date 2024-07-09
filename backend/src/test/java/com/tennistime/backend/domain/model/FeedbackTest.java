package com.tennistime.backend.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    private Feedback feedback;

    @BeforeEach
    void setUp() {
        feedback = new Feedback(1L, "Great club!", 5, LocalDateTime.now(), null, null);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, feedback.getId());
        assertEquals("Great club!", feedback.getComment());
        assertEquals(5, feedback.getRating());
        assertNotNull(feedback.getCreatedAt());
        assertNull(feedback.getClub());
        assertNull(feedback.getCourt());
    }
}
