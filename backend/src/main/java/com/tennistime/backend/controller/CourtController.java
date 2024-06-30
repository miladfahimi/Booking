package com.tennistime.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tennistime.backend.model.Court;
import com.tennistime.backend.repository.CourtRepository;

import java.util.List;

@RestController
@RequestMapping("/courts")
public class CourtController {
    @Autowired
    private CourtRepository courtRepository;

    @GetMapping
    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    @PostMapping
    public Court createCourt(@RequestBody Court court) {
        return courtRepository.save(court);
    }
}
