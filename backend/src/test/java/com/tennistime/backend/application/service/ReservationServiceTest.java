package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.mapper.ReservationMapper;
import com.tennistime.backend.domain.model.Reservation;
import com.tennistime.backend.domain.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnAllReservations() {
        Reservation reservation = new Reservation();
        ReservationDTO reservationDTO = new ReservationDTO();
        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);

        List<ReservationDTO> reservations = reservationService.findAll();

        assertEquals(1, reservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void save_shouldSaveAndReturnReservation() {
        Reservation reservation = new Reservation();
        ReservationDTO reservationDTO = new ReservationDTO();
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toEntity(any(ReservationDTO.class))).thenReturn(reservation);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);

        ReservationDTO savedReservation = reservationService.save(reservationDTO);
        assertEquals(reservationDTO, savedReservation);
    }

    @Test
    void findById_shouldReturnReservationWhenExists() {
        Reservation reservation = new Reservation();
        ReservationDTO reservationDTO = new ReservationDTO();
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);

        ReservationDTO foundReservation = reservationService.findById(1L);
        assertEquals(reservationDTO, foundReservation);
    }

    @Test
    void findById_shouldReturnNullWhenNotExists() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        ReservationDTO foundReservation = reservationService.findById(1L);
        assertNull(foundReservation);
    }
}
