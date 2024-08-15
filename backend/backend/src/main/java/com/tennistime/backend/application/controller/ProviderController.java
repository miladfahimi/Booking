package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.dto.ProviderDTO;
import com.tennistime.backend.application.dto.CourtDTO;
import com.tennistime.backend.application.service.ProviderService;
import com.tennistime.backend.application.service.CourtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/providers")
@Tag(name = "Provider Management", description = "Operations related to provider management")
public class ProviderController {

    private final ProviderService providerService;
    private final CourtService courtService;

    @Autowired
    public ProviderController(ProviderService providerService, CourtService courtService) {
        this.providerService = providerService;
        this.courtService = courtService;
    }

    @GetMapping
    @Operation(summary = "Get all providers")
    public List<ProviderDTO> getAllProviders() {
        return providerService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get provider by ID")
    public ResponseEntity<ProviderDTO> getProviderById(@PathVariable UUID id) {
        ProviderDTO provider = providerService.findById(id);
        return provider != null ? ResponseEntity.ok(provider) : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new provider")
    public ResponseEntity<ProviderDTO> createProvider(@RequestBody ProviderDTO providerDTO) {
        ProviderDTO createdProvider = providerService.save(providerDTO);
        return new ResponseEntity<>(createdProvider, HttpStatus.CREATED);
    }

    @PostMapping(value = "/{providerId}/courts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add a court to a provider")
    public ResponseEntity<ProviderDTO> addCourtToProvider(@PathVariable UUID providerId, @RequestBody CourtDTO courtDTO) {
        ProviderDTO updatedProvider = providerService.addCourtToProvider(providerId, courtDTO);
        return updatedProvider != null ? ResponseEntity.ok(updatedProvider) : ResponseEntity.notFound().build();
    }
}
