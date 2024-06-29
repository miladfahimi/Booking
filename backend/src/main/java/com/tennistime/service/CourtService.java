package com.tennistime.service;

import com.tennistime.model.Court;
import com.tennistime.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtService {

    @Autowired
    private CourtRepository courtRepository;

    public List<Court> findAll() {
        return courtRepository.findAll();
    }

    public Court save(Court court) {
        return courtRepository.save(court);
    }
}
