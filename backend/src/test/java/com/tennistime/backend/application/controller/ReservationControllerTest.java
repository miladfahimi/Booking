package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.service.ReservationService;
import com.tennistime.backend.domain.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    }

    @Test
    void getAllReservations_whenInvoked_thenReturnsReservationList() {
        Reservation reservation = new Reservation();
        when(reservationService.findAllReservations()).thenReturn(Collections.singletonList(reservation));

        ResponseEntity<List<Reservation>> responseEntity = reservationController.getAllReservations();
        List<Reservation> reservations = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, reservations.size());
        verify(reservationService, times(1)).findAllReservations();
    }

    @Test
    void createReservation_whenValidReservation_thenReturnsCreatedReservation() {
        Reservation reservation = new Reservation();
        when(reservationService.createReservation(any(Reservation.class))).thenReturn(reservation);

        ResponseEntity<Reservation> responseEntity = reservationController.createReservation(reservation);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(reservation, responseEntity.getBody());
        verify(reservationService, times(1)).createReservation(any(Reservation.class));
    }

    @Test
    void getReservationById_whenValidId_thenReturnsReservation() {
        Reservation reservation = new Reservation();
        when(reservationService.findReservationById(1L)).thenReturn(reservation);

        ResponseEntity<Reservation> responseEntity = reservationController.getReservationById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reservation, responseEntity.getBody());
        verify(reservationService, times(1)).findReservationById(1L);
    }

    @Test
    void getReservationById_whenInvalidId_thenReturnsNotFound() {
        when(reservationService.findReservationById(1L)).thenReturn(null);

        ResponseEntity<Reservation> responseEntity = reservationController.getReservationById(1L);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(reservationService, times(1)).findReservationById(1L);
    }

    @Test
    void deleteReservation_whenValidId_thenReturnsNoContent() {
        doNothing().when(reservationService).deleteReservation(1L);

        ResponseEntity<Void> responseEntity = reservationController.deleteReservation(1L);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(reservationService, times(1)).deleteReservation(1L);
    }
}
