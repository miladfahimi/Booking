package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.Club;
import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClubService {

    private final ClubRepository clubRepository;

    @Autowired
    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public List<Club> findAllClubs() {
        return clubRepository.findAll();
    }

    public Club saveClub(Club club){
        return clubRepository.save(club);
    }

    public Club findClubById(Long id) {
        Optional<Club> club = clubRepository.findById(id);
        return club.orElse(null);
    }

    public Club addCourtToClub(Long clubId, Court court) {
        Optional<Club> clubOptional = clubRepository.findById(clubId);
        if (clubOptional.isPresent()) {
            Club club = clubOptional.get();
            court.setClub(club);
            club.getCourts().add(court);
            return clubRepository.save(club);
        }
        return null;
    }
}
