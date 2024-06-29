package com.tennistime.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tennistime.model.Court;
import com.tennistime.service.CourtService;

@RestController
@RequestMapping("/api/courts")
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
