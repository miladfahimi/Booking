package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.ClubDTO;
import com.tennistime.backend.application.dto.CourtDTO;
import com.tennistime.backend.application.service.ClubService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ClubControllerTest {

    @Mock
    private ClubService clubService;

    @InjectMocks
    private ClubController clubController;

    private MockMvc mockMvc;
    private ClubDTO clubDTO;
    private CourtDTO courtDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clubController).build();
        clubDTO = new ClubDTO();
        clubDTO.setName("Test Club");

        courtDTO = new CourtDTO();
        courtDTO.setName("Test Court");
    }

    @Test
    void getAllClubs_whenInvoked_thenReturnsClubList() throws Exception {
        when(clubService.findAll()).thenReturn(Collections.singletonList(clubDTO));

        mockMvc.perform(get("/clubs"))
                .andExpect(status().isOk());

        verify(clubService, times(1)).findAll();
    }

    @Test
    void createClub_whenValidClub_thenReturnsCreatedClub() throws Exception {
        when(clubService.save(any(ClubDTO.class))).thenReturn(clubDTO);

        mockMvc.perform(post("/clubs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Club\"}"))
                .andExpect(status().isCreated());

        verify(clubService, times(1)).save(any(ClubDTO.class));
    }

    @Test
    void getClubById_whenValidId_thenReturnsClub() throws Exception {
        when(clubService.findById(anyLong())).thenReturn(clubDTO);

        mockMvc.perform(get("/clubs/1"))
                .andExpect(status().isOk());

        verify(clubService, times(1)).findById(1L);
    }

    @Test
    void addCourtToClub_whenValidCourt_thenReturnsUpdatedClub() throws Exception {
        when(clubService.addCourtToClub(anyLong(), any(CourtDTO.class))).thenReturn(clubDTO);

        mockMvc.perform(post("/clubs/1/courts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Court\"}"))
                .andExpect(status().isOk());

        verify(clubService, times(1)).addCourtToClub(eq(1L), any(CourtDTO.class));
    }
}
