package com.tennistime.backend.controller;

import com.tennistime.backend.model.Court;
import com.tennistime.backend.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courts")
public class CourtController {

    @Autowired
    private CourtService courtService;

    @GetMapping
    public List<Court> getAllCourts() {
        return courtService.getAllCourts();
    }

    @PostMapping
    public Court createCourt(@RequestBody Court court) {
        return courtService.saveCourt(court);
    }
}
