package com.tennistime.backend.application.service;


import com.tennistime.backend.application.dto.ProviderDTO;
import com.tennistime.backend.application.dto.CourtDTO;
import com.tennistime.backend.application.mapper.ProviderMapper;
import com.tennistime.backend.application.mapper.CourtMapper;
import com.tennistime.backend.domain.model.Provider;
import com.tennistime.backend.domain.model.Court;
import com.tennistime.backend.domain.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final CourtMapper courtMapper;

    @Autowired
    public ProviderService(ProviderRepository providerRepository, ProviderMapper providerMapper, CourtMapper courtMapper) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
        this.courtMapper = courtMapper;
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

    public ProviderDTO addCourtToProvider(UUID providerId, CourtDTO courtDTO) {
        Optional<Provider> providerOptional = providerRepository.findById(providerId);
        if (providerOptional.isPresent()) {
            Provider provider = providerOptional.get();
            Court court = courtMapper.toEntity(courtDTO);
            court.setProvider(provider);
            provider.getCourts().add(court);
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
