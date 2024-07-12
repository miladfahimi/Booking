package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.CourtDTO;
import com.tennistime.backend.application.service.CourtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CourtControllerTest {

    @Mock
    private CourtService courtService;

    @InjectMocks
    private CourtController courtController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courtController).build();
    }

    @Test
    void getAllCourts_whenInvoked_thenReturnsCourtList() throws Exception {
        CourtDTO courtDTO = new CourtDTO(1L, "Court 1", "Clay", true, 1L);
        when(courtService.findAll()).thenReturn(Collections.singletonList(courtDTO));

        mockMvc.perform(get("/api/courts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Court 1"))
                .andExpect(jsonPath("$[0].type").value("Clay"))
                .andExpect(jsonPath("$[0].availability").value(true))
                .andExpect(jsonPath("$[0].clubId").value(1L));

        verify(courtService, times(1)).findAll();
    }

    @Test
    void createCourt_whenValidCourt_thenReturnsCreatedCourt() throws Exception {
        CourtDTO courtDTO = new CourtDTO(1L, "Court 1", "Clay", true, 1L);
        when(courtService.save(any(CourtDTO.class))).thenReturn(courtDTO);

        mockMvc.perform(post("/api/courts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Court 1\", \"type\": \"Clay\", \"availability\": true, \"clubId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Court 1"))
                .andExpect(jsonPath("$.type").value("Clay"))
                .andExpect(jsonPath("$.availability").value(true))
                .andExpect(jsonPath("$.clubId").value(1L));

        verify(courtService, times(1)).save(any(CourtDTO.class));
    }

    @Test
    void getCourtById_whenValidId_thenReturnsCourt() throws Exception {
        CourtDTO courtDTO = new CourtDTO(1L, "Court 1", "Clay", true, 1L);
        when(courtService.findById(1L)).thenReturn(courtDTO);

        mockMvc.perform(get("/api/courts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Court 1"))
                .andExpect(jsonPath("$.type").value("Clay"))
                .andExpect(jsonPath("$.availability").value(true))
                .andExpect(jsonPath("$.clubId").value(1L));

        verify(courtService, times(1)).findById(1L);
    }
}
