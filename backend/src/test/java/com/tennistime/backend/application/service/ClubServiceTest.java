package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.ClubDTO;
import com.tennistime.backend.application.dto.CourtDTO;
import com.tennistime.backend.application.mapper.ClubMapper;
import com.tennistime.backend.application.mapper.CourtMapper;
import com.tennistime.backend.domain.model.Club;
import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.repository.ClubRepository;
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

class ClubServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private ClubMapper clubMapper;

    @Mock
    private CourtMapper courtMapper;

    @InjectMocks
    private ClubService clubService;

    private Club club;
    private ClubDTO clubDTO;
    private CourtDTO courtDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        club = new Club();
        clubDTO = new ClubDTO();
        courtDTO = new CourtDTO();
    }

    @Test
    void findAll_shouldReturnAllClubs() {
        when(clubRepository.findAll()).thenReturn(Arrays.asList(club));
        when(clubMapper.toDTO(any(Club.class))).thenReturn(clubDTO);

        assertEquals(1, clubService.findAll().size());
    }

    @Test
    void save_shouldSaveAndReturnClub() {
        when(clubRepository.save(any(Club.class))).thenReturn(club);
        when(clubMapper.toDTO(any(Club.class))).thenReturn(clubDTO);
        when(clubMapper.toEntity(any(ClubDTO.class))).thenReturn(club);

        ClubDTO savedClub = clubService.save(clubDTO);
        assertEquals(clubDTO, savedClub);
    }

    @Test
    void findById_shouldReturnClubWhenExists() {
        when(clubRepository.findById(anyLong())).thenReturn(Optional.of(club));
        when(clubMapper.toDTO(any(Club.class))).thenReturn(clubDTO);

        ClubDTO foundClub = clubService.findById(1L);
        assertEquals(clubDTO, foundClub);
    }

    @Test
    void findById_shouldReturnNullWhenNotExists() {
        when(clubRepository.findById(anyLong())).thenReturn(Optional.empty());

        ClubDTO foundClub = clubService.findById(1L);
        assertNull(foundClub);
    }

    @Test
    void addCourtToClub_shouldAddCourtAndReturnUpdatedClub() {
        when(clubRepository.findById(anyLong())).thenReturn(Optional.of(club));
        when(clubRepository.save(any(Club.class))).thenReturn(club);
        when(clubMapper.toDTO(any(Club.class))).thenReturn(clubDTO);
        when(courtMapper.toEntity(any(CourtDTO.class))).thenReturn(new Court());

        ClubDTO updatedClub = clubService.addCourtToClub(1L, courtDTO);
        assertEquals(clubDTO, updatedClub);
    }
}
