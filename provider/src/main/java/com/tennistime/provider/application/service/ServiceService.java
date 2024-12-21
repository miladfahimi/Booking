package com.tennistime.provider.application.service;

import com.tennistime.provider.application.dto.ServiceDTO;
import com.tennistime.provider.application.mapper.ServiceMapper;
import com.tennistime.provider.domain.model.Service;
import com.tennistime.provider.domain.repository.ServiceRepository;
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

    /**
     * Fetches all available services.
     *
     * @return a list of ServiceDTO objects representing all services
     */
    public List<ServiceDTO> findAll() {
        return serviceRepository.findAll().stream()
                .map(service -> {
                    ServiceDTO dto = serviceMapper.toDTO(service);
                    dto.setTags(convertStringToList(service.getTags())); // Convert tags from String to List
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Fetches a specific service by its ID.
     *
     * @param id the UUID of the service
     * @return the ServiceDTO object representing the service, or null if not found
     */
    public ServiceDTO findById(UUID id) {
        Optional<Service> service = serviceRepository.findById(id);
        return service.map(s -> {
            ServiceDTO dto = serviceMapper.toDTO(s);
            dto.setTags(convertStringToList(s.getTags())); // Convert tags from String to List
            return dto;
        }).orElse(null);
    }

    /**
     * Fetches all services provided by a specific provider.
     *
     * @param providerId the UUID of the provider
     * @return a list of ServiceDTO objects associated with the specified provider
     */
    public List<ServiceDTO> findByProviderId(UUID providerId) {
        return serviceRepository.findByProviderId(providerId).stream()
                .map(service -> {
                    ServiceDTO dto = serviceMapper.toDTO(service);
                    dto.setTags(convertStringToList(service.getTags())); // Convert tags from String to List
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Saves a new service or updates an existing service.
     *
     * @param serviceDTO the ServiceDTO object containing the service details
     * @return the saved ServiceDTO object
     */
    public ServiceDTO save(ServiceDTO serviceDTO) {
        Service service = serviceMapper.toEntity(serviceDTO);
        service.setTags(convertListToString(serviceDTO.getTags())); // Convert tags from List to String
        Service savedService = serviceRepository.save(service);
        return serviceMapper.toDTO(savedService);
    }

    /**
     * Deletes a service by its ID.
     *
     * @param id the UUID of the service to be deleted
     */
    public void deleteById(UUID id) {
        serviceRepository.deleteById(id);
    }

    /**
     * Converts a comma-separated string to a list of tags.
     *
     * @param tags the comma-separated string of tags
     * @return a list of tags
     */
    private List<String> convertStringToList(String tags) {
        return tags != null ? Arrays.asList(tags.split(",")) : null;
    }

    /**
     * Converts a list of tags to a comma-separated string.
     *
     * @param tags the list of tags
     * @return a comma-separated string of tags
     */
    private String convertListToString(List<String> tags) {
        return tags != null ? String.join(",", tags) : null;
    }
}
