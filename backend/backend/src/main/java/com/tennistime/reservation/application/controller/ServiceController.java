package com.tennistime.reservation.application.controller;

import com.tennistime.reservation.application.dto.ServiceDTO;
import com.tennistime.reservation.application.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/services")
@Tag(name = "Service Management", description = "Operations related to service management")
public class ServiceController {

    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    @Operation(summary = "Get all services")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        List<ServiceDTO> services = serviceService.findAll();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get service by ID")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable UUID id) {
        ServiceDTO service = serviceService.findById(id);
        return service != null ? ResponseEntity.ok(service) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new service")
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO serviceDTO) {
        ServiceDTO savedService = serviceService.save(serviceDTO);
        return ResponseEntity.ok(savedService);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a service by ID")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable UUID id, @RequestBody ServiceDTO serviceDTO) {
        serviceDTO.setId(id);
        ServiceDTO updatedService = serviceService.save(serviceDTO);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a service by ID")
    public ResponseEntity<Void> deleteService(@PathVariable UUID id) {
        serviceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
