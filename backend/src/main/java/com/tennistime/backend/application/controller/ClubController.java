package com.tennistime.backend.application.controller;

import com.tennistime.backend.domain.model.Club;
import com.tennistime.backend.application.service.ClubService;
import com.tennistime.backend.domain.model.Court;
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

    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping
    public List<Club> getAllClubs() {
        return clubService.findAllClubs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Club> getClubById(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.findClubById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Club> createClub(@RequestBody Club club) {
        Club createdClub = clubService.saveClub(club);
        return new ResponseEntity<>(createdClub, HttpStatus.CREATED);
    }

    @PostMapping(value = "/{clubId}/courts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Club addCourtToClub(@PathVariable Long clubId, @RequestBody Court court) {
        return clubService.addCourtToClub(clubId, court);
    }
}
