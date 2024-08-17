package com.tennistime.backend.application.service;


import com.tennistime.backend.application.dto.ProviderDTO;
import com.tennistime.backend.application.dto.ServiceDTO;
import com.tennistime.backend.application.mapper.ProviderMapper;
import com.tennistime.backend.application.mapper.ServiceMapper;
import com.tennistime.backend.domain.model.Provider;
import com.tennistime.backend.domain.model.Service;
import com.tennistime.backend.domain.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final ServiceMapper serviceMapper;

    @Autowired
    public ProviderService(ProviderRepository providerRepository, ProviderMapper providerMapper, ServiceMapper serviceMapper) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
        this.serviceMapper = serviceMapper;
    }

    public List<ProviderDTO> findAll() {
        return providerRepository.findAll()
                .stream()
                .map(providerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProviderDTO findById(UUID id) {
        Optional<Provider> provider = providerRepository.findById(id);
        return provider.map(providerMapper::toDTO).orElse(null);
    }

    public ProviderDTO save(ProviderDTO providerDTO) {
        Provider provider = providerMapper.toEntity(providerDTO);
        Provider savedProvider = providerRepository.save(provider);
        return providerMapper.toDTO(savedProvider);
    }

    public ProviderDTO addServiceToProvider(UUID providerId, ServiceDTO serviceDTO) {
        Optional<Provider> providerOptional = providerRepository.findById(providerId);
        if (providerOptional.isPresent()) {
            Provider provider = providerOptional.get();
            Service service = serviceMapper.toEntity(serviceDTO);
            service.setProvider(provider);
            provider.getServices().add(service);
            Provider updatedProvider = providerRepository.save(provider);
            return providerMapper.toDTO(updatedProvider);
        } else {
            return null;
        }
    }

    public void deleteById(UUID id) {
        providerRepository.deleteById(id);
    }
}
