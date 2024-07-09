package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.service.ClubService;
import com.tennistime.backend.domain.model.Club;
import com.tennistime.backend.domain.model.Court;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ClubControllerTest {

    @Mock
    private ClubService clubService;

    @InjectMocks
    private ClubController clubController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clubController).build();
    }

    @Test
    void getAllClubs_whenCalled_thenReturnClubList() throws Exception {
        List<Club> clubs = Arrays.asList(
                new Club(1L, "Club 1", "Address 1", "1234567890", "club1@example.com", "Description 1", new ArrayList<>(), new ArrayList<>()),
                new Club(2L, "Club 2", "Address 2", "0987654321", "club2@example.com", "Description 2", new ArrayList<>(), new ArrayList<>())
        );
        when(clubService.findAllClubs()).thenReturn(clubs);

        mockMvc.perform(get("/clubs"))
                .andExpect(status().isOk());
    }

    @Test
    void getClubById_whenValidId_thenReturnClub() throws Exception {
        Club club = new Club(1L, "Club 1", "Address 1", "1234567890", "club1@example.com", "Description 1", new ArrayList<>(), new ArrayList<>());
        when(clubService.findClubById(anyLong())).thenReturn(club);

        ResponseEntity<Club> response = clubController.getClubById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(club, response.getBody());
    }

    @Test
    void createClub_whenValidClub_thenReturnSavedClub() {
        Club club = new Club(null, "New Club", "New Address", "1231231234", "newclub@example.com", "New Description", new ArrayList<>(), new ArrayList<>());
        Club savedClub = new Club(1L, "New Club", "New Address", "1231231234", "newclub@example.com", "New Description", new ArrayList<>(), new ArrayList<>());
        when(clubService.saveClub(any(Club.class))).thenReturn(savedClub);

        ResponseEntity<Club> response = clubController.createClub(club);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedClub, response.getBody());
    }

    @Test
    void addCourtToClub_whenValidClubIdAndCourt_thenReturnUpdatedClub() {
        Court court = new Court(null, "Court 1", "Clay", true, null, new ArrayList<>(), new ArrayList<>());
        Club club = new Club(1L, "Club 1", "Address 1", "1234567890", "club1@example.com", "Description 1", new ArrayList<>(), new ArrayList<>());
        club.getCourts().add(court);
        when(clubService.addCourtToClub(anyLong(), any(Court.class))).thenReturn(club);

        Club response = clubController.addCourtToClub(1L, court);
        assertNotNull(response);
        assertEquals(1, response.getCourts().size());
    }
}
