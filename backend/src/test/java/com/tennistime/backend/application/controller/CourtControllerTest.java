package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.service.CourtService;
import com.tennistime.backend.domain.model.Court;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CourtControllerTest {

    @Mock
    private CourtService courtService;

    @InjectMocks
    private CourtController courtController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courtController).build();
    }

    @Test
    void getAllCourts_whenInvoked_thenReturnsCourtList() {
        Court court = new Court();
        when(courtService.findAllCourts()).thenReturn(Collections.singletonList(court));

        List<Court> courts = courtController.getAllCourts();

        assertEquals(1, courts.size());
        verify(courtService, times(1)).findAllCourts();
    }

    @Test
    void createCourt_whenValidCourt_thenReturnsCreatedCourt() {
        Court court = new Court();
        when(courtService.saveCourt(any(Court.class))).thenReturn(court);

        Court createdCourt = courtController.createCourt(court);

        assertEquals(court, createdCourt);
        verify(courtService, times(1)).saveCourt(any(Court.class));
    }

    @Test
    void getCourtById_whenValidId_thenReturnsCourt() {
        Court court = new Court();
        when(courtService.findCourtById(1L)).thenReturn(court);

        Court foundCourt = courtController.getCourtById(1L);

        assertEquals(court, foundCourt);
        verify(courtService, times(1)).findCourtById(1L);
    }
}
