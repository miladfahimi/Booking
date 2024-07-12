package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.CourtDTO;
import com.tennistime.backend.application.mapper.CourtMapper;
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

    @Mock
    private CourtMapper courtMapper;

    @InjectMocks
    private CourtService courtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllCourts_shouldReturnAllCourts() {
        Court court = new Court();
        CourtDTO courtDTO = new CourtDTO();
        when(courtRepository.findAll()).thenReturn(Arrays.asList(court));
        when(courtMapper.toDTO(any(Court.class))).thenReturn(courtDTO);

        assertEquals(1, courtService.findAll().size());
    }

    @Test
    void saveCourt_shouldSaveAndReturnCourt() {
        Court court = new Court();
        CourtDTO courtDTO = new CourtDTO();
        when(courtRepository.save(any(Court.class))).thenReturn(court);
        when(courtMapper.toEntity(any(CourtDTO.class))).thenReturn(court);
        when(courtMapper.toDTO(any(Court.class))).thenReturn(courtDTO);

        CourtDTO savedCourt = courtService.save(courtDTO);
        assertEquals(courtDTO, savedCourt);
    }

    @Test
    void findCourtById_shouldReturnCourtWhenExists() {
        Court court = new Court();
        CourtDTO courtDTO = new CourtDTO();
        when(courtRepository.findById(anyLong())).thenReturn(Optional.of(court));
        when(courtMapper.toDTO(any(Court.class))).thenReturn(courtDTO);

        CourtDTO foundCourt = courtService.findById(1L);
        assertEquals(courtDTO, foundCourt);
    }

    @Test
    void findCourtById_shouldReturnNullWhenNotExists() {
        when(courtRepository.findById(anyLong())).thenReturn(Optional.empty());

        CourtDTO foundCourt = courtService.findById(1L);
        assertNull(foundCourt);
    }
}
