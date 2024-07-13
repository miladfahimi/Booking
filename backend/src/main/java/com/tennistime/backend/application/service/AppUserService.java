package com.tennistime.backend.application.service;

import com.tennistime.backend.application.dto.AppUserDTO;
import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.application.mapper.AppUserMapper;
import com.tennistime.backend.application.mapper.ReservationMapper;
import com.tennistime.backend.domain.model.AppUser;
import com.tennistime.backend.domain.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final ReservationMapper reservationMapper;

    public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper, ReservationMapper reservationMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
        this.reservationMapper = reservationMapper;
    }

    public List<AppUserDTO> findAll() {
        return appUserRepository.findAll().stream()
                .map(appUserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AppUserDTO save(AppUserDTO appUserDTO) {
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        appUser = appUserRepository.save(appUser);
        return appUserMapper.toDTO(appUser);
    }

    public AppUserDTO findById(Long id) {
        return appUserRepository.findById(id)
                .map(appUserMapper::toDTO)
                .orElse(null);
    }

    public void deleteById(Long id) {
        appUserRepository.deleteById(id);
    }

    public List<ReservationDTO> findReservationsByUserId(Long userId) {
        Optional<AppUser> user = appUserRepository.findById(userId);
        if (user.isPresent()) {
            return user.get().getReservations().stream()
                    .map(reservationMapper::toDTO)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
}
