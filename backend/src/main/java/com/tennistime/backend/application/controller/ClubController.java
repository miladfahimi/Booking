package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.ClubDTO;
import com.tennistime.backend.application.dto.CourtDTO;
import com.tennistime.backend.application.service.ClubService;
import com.tennistime.backend.application.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;
    private final CourtService courtService;

    @Autowired
    public ClubController(ClubService clubService, CourtService courtService) {
        this.clubService = clubService;
        this.courtService = courtService;
    }

    @GetMapping
    public List<ClubDTO> getAllClubs() {
        return clubService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubDTO> getClubById(@PathVariable Long id) {
        ClubDTO club = clubService.findById(id);
        return club != null ? ResponseEntity.ok(club) : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClubDTO> createClub(@RequestBody ClubDTO clubDTO) {
        ClubDTO createdClub = clubService.save(clubDTO);
        return new ResponseEntity<>(createdClub, HttpStatus.CREATED);
    }

    @PostMapping(value = "/{clubId}/courts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClubDTO> addCourtToClub(@PathVariable Long clubId, @RequestBody CourtDTO courtDTO) {
        ClubDTO updatedClub = clubService.addCourtToClub(clubId, courtDTO);
        return updatedClub != null ? ResponseEntity.ok(updatedClub) : ResponseEntity.notFound().build();
    }
}
