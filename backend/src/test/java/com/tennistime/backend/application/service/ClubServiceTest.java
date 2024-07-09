package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.Club;
import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.repository.ClubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClubServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private ClubService clubService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllClubs_shouldReturnAllClubs() {
        Club club = new Club();
        when(clubRepository.findAll()).thenReturn(Arrays.asList(club));

        assertEquals(1, clubService.findAllClubs().size());
    }

    @Test
    void saveClub_shouldSaveAndReturnClub() {
        Club club = new Club();
        when(clubRepository.save(any(Club.class))).thenReturn(club);

        Club savedClub = clubService.saveClub(club);
        assertEquals(club, savedClub);
    }

    @Test
    void findClubById_shouldReturnClubWhenExists() {
        Club club = new Club();
        when(clubRepository.findById(anyLong())).thenReturn(Optional.of(club));

        Club foundClub = clubService.findClubById(1L);
        assertEquals(club, foundClub);
    }

    @Test
    void findClubById_shouldReturnNullWhenNotExists() {
        when(clubRepository.findById(anyLong())).thenReturn(Optional.empty());

        Club foundClub = clubService.findClubById(1L);
        assertNull(foundClub);
    }

    @Test
    void addCourtToClub_shouldAddCourtToClub() {
        Club club = new Club();
        club.setCourts(new ArrayList<>()); // Initialize the list of courts
        Court court = new Court();
        when(clubRepository.findById(anyLong())).thenReturn(Optional.of(club));
        when(clubRepository.save(any(Club.class))).thenReturn(club);

        Club updatedClub = clubService.addCourtToClub(1L, court);
        assertNotNull(updatedClub);
        assertEquals(1, updatedClub.getCourts().size());
        verify(clubRepository, times(1)).save(club);
    }
}
