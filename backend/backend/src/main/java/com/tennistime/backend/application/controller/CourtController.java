package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.CourtDTO;
import com.tennistime.backend.application.service.CourtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courts")
@Tag(name = "Court Management", description = "Operations related to court management")
public class CourtController {

    private final CourtService courtService;

    @Autowired
    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @GetMapping
    @Operation(summary = "Get all courts")
    public ResponseEntity<List<CourtDTO>> getAllCourts() {
        List<CourtDTO> courts = courtService.findAll();
        return ResponseEntity.ok(courts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get court by ID")
    public ResponseEntity<CourtDTO> getCourtById(@PathVariable Long id) {
        CourtDTO court = courtService.findById(id);
        return court != null ? ResponseEntity.ok(court) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new court")
    public ResponseEntity<CourtDTO> createCourt(@RequestBody CourtDTO courtDTO) {
        CourtDTO savedCourt = courtService.save(courtDTO);
        return ResponseEntity.ok(savedCourt);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a court by ID")
    public ResponseEntity<CourtDTO> updateCourt(@PathVariable Long id, @RequestBody CourtDTO courtDTO) {
        courtDTO.setId(id);
        CourtDTO updatedCourt = courtService.save(courtDTO);
        return ResponseEntity.ok(updatedCourt);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a court by ID")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) {
        courtService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
