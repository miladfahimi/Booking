package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.AppUserDTO;
import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.service.AppUserService;
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

class AppUserControllerTest {

    @Mock
    private AppUserService appUserService;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private AppUserController appUserController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(appUserController).build();
    }

    @Test
    void getAllUsers_shouldReturnUserList() throws Exception {
        AppUserDTO appUserDTO = new AppUserDTO();
        when(appUserService.findAll()).thenReturn(Collections.singletonList(appUserDTO));

        List<AppUserDTO> users = appUserController.getAllUsers().getBody();

        assertEquals(1, users.size());
        verify(appUserService, times(1)).findAll();

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        AppUserDTO appUserDTO = new AppUserDTO();
        when(appUserService.findById(1L)).thenReturn(appUserDTO);

        AppUserDTO user = appUserController.getUserById(1L).getBody();

        assertEquals(appUserDTO, user);
        verify(appUserService, times(1)).findById(1L);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_shouldCreateAndReturnUser() throws Exception {
        AppUserDTO appUserDTO = new AppUserDTO();
        when(appUserService.save(any(AppUserDTO.class))).thenReturn(appUserDTO);

        AppUserDTO createdUser = appUserController.createUser(appUserDTO).getBody();

        assertEquals(appUserDTO, createdUser);
        verify(appUserService, times(1)).save(any(AppUserDTO.class));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_shouldDeleteUser() throws Exception {
        doNothing().when(appUserService).deleteById(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(appUserService, times(1)).deleteById(1L);
    }

    @Test
    void getReservationsByUserId_shouldReturnReservationList() throws Exception {
        ReservationDTO reservationDTO = new ReservationDTO();
        when(reservationService.findReservationsByUserId(1L)).thenReturn(Collections.singletonList(reservationDTO));

        List<ReservationDTO> reservations = appUserController.getReservationsByUserId(1L).getBody();

        assertEquals(1, reservations.size());
        verify(reservationService, times(1)).findReservationsByUserId(1L);

        mockMvc.perform(get("/users/1/reservations"))
                .andExpect(status().isOk());
    }
}
