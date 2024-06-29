package com.tennistime.controller;

import com.tennistime.model.Court;
import com.tennistime.service.CourtService;
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
        return courtService.findAll();
    }

    @PostMapping
    public Court createCourt(@RequestBody Court court) {
        return courtService.save(court);
    }
}
