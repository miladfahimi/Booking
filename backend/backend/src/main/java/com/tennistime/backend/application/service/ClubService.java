package com.tennistime.backend.application.service;


import com.tennistime.backend.application.dto.ClubDTO;
import com.tennistime.backend.application.dto.CourtDTO;
import com.tennistime.backend.application.mapper.ClubMapper;
import com.tennistime.backend.application.mapper.CourtMapper;
import com.tennistime.backend.domain.model.Club;
import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
    private final CourtMapper courtMapper;

    @Autowired
    public ClubService(ClubRepository clubRepository, ClubMapper clubMapper, CourtMapper courtMapper) {
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
        this.courtMapper = courtMapper;
    }

    public List<ClubDTO> findAll() {
        return clubRepository.findAll()
                .stream()
                .map(clubMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClubDTO findById(Long id) {
        Optional<Club> club = clubRepository.findById(id);
        return club.map(clubMapper::toDTO).orElse(null);
    }

    public ClubDTO save(ClubDTO clubDTO) {
        Club club = clubMapper.toEntity(clubDTO);
        Club savedClub = clubRepository.save(club);
        return clubMapper.toDTO(savedClub);
    }

    public ClubDTO addCourtToClub(Long clubId, CourtDTO courtDTO) {
        Optional<Club> clubOptional = clubRepository.findById(clubId);
        if (clubOptional.isPresent()) {
            Club club = clubOptional.get();
            Court court = courtMapper.toEntity(courtDTO);
            court.setClub(club);
            club.getCourts().add(court);
            Club updatedClub = clubRepository.save(club);
            return clubMapper.toDTO(updatedClub);
        } else {
            return null;
        }
    }

    public void deleteById(Long id) {
        clubRepository.deleteById(id);
    }
}
