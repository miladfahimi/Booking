package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.CourtDTO;
import com.tennistime.backend.application.mapper.CourtMapper;
import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourtService {

    private final CourtRepository courtRepository;
    private final CourtMapper courtMapper;

    @Autowired
    public CourtService(CourtRepository courtRepository, CourtMapper courtMapper) {
        this.courtRepository = courtRepository;
        this.courtMapper = courtMapper;
    }

    public List<CourtDTO> findAll() {
        return courtRepository.findAll().stream()
                .map(courtMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CourtDTO findById(Long id) {
        Optional<Court> court = courtRepository.findById(id);
        return court.map(courtMapper::toDTO).orElse(null);
    }

    public CourtDTO save(CourtDTO courtDTO) {
        Court court = courtMapper.toEntity(courtDTO);
        Court savedCourt = courtRepository.save(court);
        return courtMapper.toDTO(savedCourt);
    }

    public void deleteById(Long id) {
        courtRepository.deleteById(id);
    }

//    public CourtDTO deleteById(Long id) {
//        Court court = courtMapper.toEntity(courtRepository.findById(id));
//        Court deletedCourt = courtRepository.deleteById(id);
//        return courtMapper.toDTO(deletedCourt);
//    }
}
