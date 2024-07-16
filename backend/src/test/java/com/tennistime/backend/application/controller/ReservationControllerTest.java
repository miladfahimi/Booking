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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    }

    @Test
    public void testGetAllReservations() throws Exception {
        ReservationDTO reservation1 = new ReservationDTO();
        reservation1.setId(1L);
        ReservationDTO reservation2 = new ReservationDTO();
        reservation2.setId(2L);
        List<ReservationDTO> reservations = Arrays.asList(reservation1, reservation2);
        when(reservationService.findAll()).thenReturn(reservations);

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    public void testGetReservationById() throws Exception {
        ReservationDTO reservation = new ReservationDTO();
        reservation.setId(1L);
        when(reservationService.findById(anyLong())).thenReturn(reservation);

        mockMvc.perform(get("/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testCreateReservation() throws Exception {
        ReservationDTO reservation = new ReservationDTO();
        reservation.setId(1L);
        when(reservationService.save(any(ReservationDTO.class))).thenReturn(reservation);

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"reservationDate\": \"2024-08-11\", \"startTime\": \"10:00\", \"endTime\": \"12:00\", \"status\": \"confirmed\", \"userId\": 3, \"courtId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testDeleteReservation() throws Exception {
        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isNoContent());
    }
}
