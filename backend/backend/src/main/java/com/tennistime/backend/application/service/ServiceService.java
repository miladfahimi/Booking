package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.ServiceDTO;
import com.tennistime.backend.application.mapper.ServiceMapper;
import com.tennistime.backend.domain.model.Service;
import com.tennistime.backend.domain.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, ServiceMapper serviceMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
    }

    public List<ServiceDTO> findAll() {
        return serviceRepository.findAll().stream()
                .map(service -> {
                    ServiceDTO dto = serviceMapper.toDTO(service);
                    dto.setTags(convertStringToList(service.getTags())); // Convert tags from String to List
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ServiceDTO findById(UUID id) {
        Optional<Service> service = serviceRepository.findById(id);
        return service.map(s -> {
            ServiceDTO dto = serviceMapper.toDTO(s);
            dto.setTags(convertStringToList(s.getTags())); // Convert tags from String to List
            return dto;
        }).orElse(null);
    }

    public ServiceDTO save(ServiceDTO serviceDTO) {
        Service service = serviceMapper.toEntity(serviceDTO);
        service.setTags(convertListToString(serviceDTO.getTags())); // Convert tags from List to String
        Service savedService = serviceRepository.save(service);
        return serviceMapper.toDTO(savedService);
    }

    public void deleteById(UUID id) {
        serviceRepository.deleteById(id);
    }

    // Conversion methods
    private List<String> convertStringToList(String tags) {
        return tags != null ? Arrays.asList(tags.split(",")) : null;
    }

    private String convertListToString(List<String> tags) {
        return tags != null ? String.join(",", tags) : null;
    }
}
