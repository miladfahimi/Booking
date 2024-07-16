package com.tennistime.backend.application.service;

import com.github.mfathi91.time.PersianDate;
import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.mapper.ReservationMapper;
import com.tennistime.backend.domain.model.AppUser;
import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.model.Reservation;
import com.tennistime.backend.domain.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private ReservationDTO reservationDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        AppUser user = new AppUser();
        user.setId(3L);
        user.setUsername("user");
        user.setPassword("password");

        Court court = new Court();
        court.setId(1L);
        court.setName("Court 1");

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setReservationDate(LocalDate.of(2024, 8, 11));
        reservation.setStartTime(LocalTime.of(10, 0));
        reservation.setEndTime(LocalTime.of(12, 0));
        reservation.setStatus("confirmed");
        reservation.setUser(user);
        reservation.setCourt(court);

        reservationDTO = new ReservationDTO();
        reservationDTO.setId(1L);
        reservationDTO.setReservationDate(LocalDate.of(2024, 8, 11));
        reservationDTO.setStartTime(LocalTime.of(10, 0));
        reservationDTO.setEndTime(LocalTime.of(12, 0));
        reservationDTO.setStatus("confirmed");
        reservationDTO.setUserId(3L);
        reservationDTO.setCourtId(1L);
        reservationDTO.setReservationDatePersian(PersianDate.fromGregorian(LocalDate.of(2024, 8, 11)).toString());
    }

    @Test
    public void testFindAll() {
        List<Reservation> reservations = Arrays.asList(reservation, reservation);
        when(reservationRepository.findAll()).thenReturn(reservations);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);

        List<ReservationDTO> reservationDTOList = reservationService.findAll();

        assertNotNull(reservationDTOList);
        assertEquals(2, reservationDTOList.size());
    }

    @Test
    public void testFindById() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);

        ReservationDTO foundReservationDTO = reservationService.findById(1L);

        assertNotNull(foundReservationDTO);
    }

    @Test
    public void testSave() {
        when(reservationMapper.toEntity(any(ReservationDTO.class))).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);

        ReservationDTO savedReservationDTO = reservationService.save(reservationDTO);

        assertNotNull(savedReservationDTO);
    }

    @Test
    public void testDeleteById() {
        reservationService.deleteById(1L);
    }

    @Test
    public void testFindReservationsByUserId() {
        List<Reservation> reservations = Arrays.asList(reservation, reservation);
        when(reservationRepository.findByUserId(anyLong())).thenReturn(reservations);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);

        List<ReservationDTO> reservationDTOList = reservationService.findReservationsByUserId(1L);

        assertNotNull(reservationDTOList);
        assertEquals(2, reservationDTOList.size());
    }

    @Test
    public void testFindReservationsByCourtId() {
        List<Reservation> reservations = Arrays.asList(reservation, reservation);
        when(reservationRepository.findByCourtId(anyLong())).thenReturn(reservations);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);

        List<ReservationDTO> reservationDTOList = reservationService.findReservationsByCourtId(1L);

        assertNotNull(reservationDTOList);
        assertEquals(2, reservationDTOList.size());
    }

    @Test
    public void testFindReservationsByPersianDate() {
        LocalDate gregorianDate = PersianDate.parse("1403-04-01").toGregorian();
        reservation.setReservationDate(gregorianDate);
        List<Reservation> reservations = Arrays.asList(reservation, reservation);
        when(reservationRepository.findAll()).thenReturn(reservations);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);

        List<ReservationDTO> reservationDTOList = reservationService.findReservationsByPersianDate("1403-04-01");

        assertNotNull(reservationDTOList);
        assertEquals(2, reservationDTOList.size());
    }
}
