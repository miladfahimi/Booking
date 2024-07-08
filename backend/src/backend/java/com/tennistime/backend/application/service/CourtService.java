package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourtService {

    private final CourtRepository courtRepository;

    @Autowired
    public CourtService(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    public List<Court> findAllCourts() {
        return courtRepository.findAll();
    }

    public Court saveCourt(Court court) {
        return courtRepository.save(court);
    }

    public Court findCourtById(Long id) {
        Optional<Court> court = courtRepository.findById(id);
        return court.orElse(null);
    }
}
