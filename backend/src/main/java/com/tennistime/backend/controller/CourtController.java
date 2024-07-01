package com.tennistime.backend.controller;

import com.tennistime.backend.model.Court;
import com.tennistime.backend.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courts")
public class CourtController {

    private final CourtRepository courtRepository;

    @Autowired
    public CourtController(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    @GetMapping
    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    @PostMapping
    public Court createCourt(@RequestBody Court court) {
        return courtRepository.save(court);
    }
}
