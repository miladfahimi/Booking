package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.Club;
import com.tennistime.backend.domain.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Club saveClub(Club club) {
        return clubRepository.save(club);
    }

}
