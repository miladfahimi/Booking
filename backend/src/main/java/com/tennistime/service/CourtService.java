package com.tennistime.backend.service;

import com.tennistime.backend.model.Court;
import com.tennistime.backend.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtService {

    @Autowired
    private CourtRepository courtRepository;

    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    public Court saveCourt(Court court) {
        return courtRepository.save(court);
    }
}
