package com.tennistime.reservation.application.service;

import com.tennistime.reservation.application.dto.ServiceDTO;
import com.tennistime.reservation.application.mapper.ServiceMapper;
import com.tennistime.reservation.domain.model.Service;
import com.tennistime.reservation.domain.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

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
                .map(serviceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ServiceDTO findById(UUID id) {
        Optional<Service> service = serviceRepository.findById(id);
        return service.map(serviceMapper::toDTO).orElse(null);
    }

    public ServiceDTO save(ServiceDTO serviceDTO) {
        Service service = serviceMapper.toEntity(serviceDTO);
        Service savedService = serviceRepository.save(service);
        return serviceMapper.toDTO(savedService);
    }

    public void deleteById(UUID id) {
        serviceRepository.deleteById(id);
    }

//    public ServiceDTO deleteById(UUID id) {
//        Service service = serviceMapper.toEntity(serviceRepository.findById(id));
//        Service deletedService = serviceRepository.deleteById(id);
//        return serviceMapper.toDTO(deletedService);
//    }
}
