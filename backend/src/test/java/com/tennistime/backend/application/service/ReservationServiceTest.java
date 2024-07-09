package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.Reservation;
import com.tennistime.backend.domain.repository.ReservationRepository;
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

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllReservations_shouldReturnAllReservations() {
        Reservation reservation = new Reservation();
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

        assertEquals(1, reservationService.findAllReservations().size());
    }

    @Test
    void createReservation_shouldSaveAndReturnReservation() {
        Reservation reservation = new Reservation();
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = reservationService.createReservation(reservation);
        assertEquals(reservation, savedReservation);
    }

    @Test
    void findReservationById_shouldReturnReservationWhenExists() {
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        Reservation foundReservation = reservationService.findReservationById(1L);
        assertEquals(reservation, foundReservation);
    }

    @Test
    void findReservationById_shouldReturnNullWhenNotExists() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        Reservation foundReservation = reservationService.findReservationById(1L);
        assertNull(foundReservation);
    }

    @Test
    void deleteReservation_shouldDeleteReservation() {
        doNothing().when(reservationRepository).deleteById(anyLong());

        reservationService.deleteReservation(1L);

        verify(reservationRepository, times(1)).deleteById(1L);
    }
}
