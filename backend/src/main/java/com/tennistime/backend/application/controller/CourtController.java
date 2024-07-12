package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.CourtDTO;
import com.tennistime.backend.application.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courts")
public class CourtController {

    private final CourtService courtService;

    @Autowired
    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @GetMapping
    public ResponseEntity<List<CourtDTO>> getAllCourts() {
        List<CourtDTO> courts = courtService.findAll();
        return ResponseEntity.ok(courts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourtDTO> getCourtById(@PathVariable Long id) {
        CourtDTO court = courtService.findById(id);
        return court != null ? ResponseEntity.ok(court) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CourtDTO> createCourt(@RequestBody CourtDTO courtDTO) {
        CourtDTO savedCourt = courtService.save(courtDTO);
        return ResponseEntity.ok(savedCourt);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourtDTO> updateCourt(@PathVariable Long id, @RequestBody CourtDTO courtDTO) {
        courtDTO.setId(id);
        CourtDTO updatedCourt = courtService.save(courtDTO);
        return ResponseEntity.ok(updatedCourt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) {
        courtService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
