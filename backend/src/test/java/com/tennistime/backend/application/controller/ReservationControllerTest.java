package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.service.ReservationService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    }

    @Test
    void getAllReservations_shouldReturnReservationList() throws Exception {
        ReservationDTO reservationDTO = new ReservationDTO();
        when(reservationService.findAll()).thenReturn(Collections.singletonList(reservationDTO));

        List<ReservationDTO> reservations = reservationController.getAllReservations().getBody();

        assertEquals(1, reservations.size());
        verify(reservationService, times(1)).findAll();

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk());
    }

    @Test
    void getReservationById_shouldReturnReservation() throws Exception {
        ReservationDTO reservationDTO = new ReservationDTO();
        when(reservationService.findById(1L)).thenReturn(reservationDTO);

        ReservationDTO reservation = reservationController.getReservationById(1L).getBody();

        assertEquals(reservationDTO, reservation);
        verify(reservationService, times(1)).findById(1L);

        mockMvc.perform(get("/reservations/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createReservation_shouldCreateAndReturnReservation() throws Exception {
        ReservationDTO reservationDTO = new ReservationDTO();
        when(reservationService.save(any(ReservationDTO.class))).thenReturn(reservationDTO);

        ReservationDTO createdReservation = reservationController.createReservation(reservationDTO).getBody();

        assertEquals(reservationDTO, createdReservation);
        verify(reservationService, times(1)).save(any(ReservationDTO.class));

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReservation_shouldDeleteReservation() throws Exception {
        doNothing().when(reservationService).deleteById(1L);

        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).deleteById(1L);
    }

    @Test
    void findReservationsByUserId_shouldReturnReservationList() throws Exception {
        ReservationDTO reservationDTO = new ReservationDTO();
        when(reservationService.findReservationsByUserId(1L)).thenReturn(Collections.singletonList(reservationDTO));

        List<ReservationDTO> reservations = reservationController.findReservationsByUserId(1L).getBody();

        assertEquals(1, reservations.size());
        verify(reservationService, times(1)).findReservationsByUserId(1L);

        mockMvc.perform(get("/reservations/user/1"))
                .andExpect(status().isOk());
    }
}
