package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.repository.CourtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourtServiceTest {

    @Mock
    private CourtRepository courtRepository;

    @InjectMocks
    private CourtService courtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllCourts_shouldReturnAllCourts() {
        Court court = new Court();
        when(courtRepository.findAll()).thenReturn(Arrays.asList(court));

        assertEquals(1, courtService.findAllCourts().size());
    }

    @Test
    void saveCourt_shouldSaveAndReturnCourt() {
        Court court = new Court();
        when(courtRepository.save(any(Court.class))).thenReturn(court);

        Court savedCourt = courtService.saveCourt(court);
        assertEquals(court, savedCourt);
    }

    @Test
    void findCourtById_shouldReturnCourtWhenExists() {
        Court court = new Court();
        when(courtRepository.findById(anyLong())).thenReturn(Optional.of(court));

        Court foundCourt = courtService.findCourtById(1L);
        assertEquals(court, foundCourt);
    }

    @Test
    void findCourtById_shouldReturnNullWhenNotExists() {
        when(courtRepository.findById(anyLong())).thenReturn(Optional.empty());

        Court foundCourt = courtService.findCourtById(1L);
        assertNull(foundCourt);
    }
}
