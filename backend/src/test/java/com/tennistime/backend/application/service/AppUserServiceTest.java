package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.AppUserDTO;
import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.mapper.AppUserMapper;
import com.tennistime.backend.application.mapper.ReservationMapper;
import com.tennistime.backend.domain.model.AppUser;
import com.tennistime.backend.domain.model.Reservation;
import com.tennistime.backend.domain.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserMapper appUserMapper;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        AppUser appUser = new AppUser();
        AppUserDTO appUserDTO = new AppUserDTO();
        when(appUserRepository.findAll()).thenReturn(Collections.singletonList(appUser));
        when(appUserMapper.toDTO(any(AppUser.class))).thenReturn(appUserDTO);

        List<AppUserDTO> users = appUserService.findAll();

        assertEquals(1, users.size());
        verify(appUserRepository, times(1)).findAll();
    }

    @Test
    void save_shouldSaveAndReturnUser() {
        AppUser appUser = new AppUser();
        AppUserDTO appUserDTO = new AppUserDTO();
        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
        when(appUserMapper.toEntity(any(AppUserDTO.class))).thenReturn(appUser);
        when(appUserMapper.toDTO(any(AppUser.class))).thenReturn(appUserDTO);

        AppUserDTO savedUser = appUserService.save(appUserDTO);
        assertEquals(appUserDTO, savedUser);
    }

    @Test
    void findById_shouldReturnUserWhenExists() {
        AppUser appUser = new AppUser();
        AppUserDTO appUserDTO = new AppUserDTO();
        when(appUserRepository.findById(anyLong())).thenReturn(Optional.of(appUser));
        when(appUserMapper.toDTO(any(AppUser.class))).thenReturn(appUserDTO);

        AppUserDTO foundUser = appUserService.findById(1L);
        assertEquals(appUserDTO, foundUser);
    }

    @Test
    void findById_shouldReturnNullWhenNotExists() {
        when(appUserRepository.findById(anyLong())).thenReturn(Optional.empty());

        AppUserDTO foundUser = appUserService.findById(1L);
        assertNull(foundUser);
    }

    @Test
    void findReservationsByUserId_shouldReturnReservationList() {
        AppUser appUser = new AppUser();
        appUser.setReservations(new ArrayList<>());
        Reservation reservation = new Reservation();
        ReservationDTO reservationDTO = new ReservationDTO();
        appUser.getReservations().add(reservation);

        when(appUserRepository.findById(anyLong())).thenReturn(Optional.of(appUser));
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDTO);

        List<ReservationDTO> reservations = appUserService.findReservationsByUserId(1L);

        assertEquals(1, reservations.size());
        verify(appUserRepository, times(1)).findById(anyLong());
    }
}
