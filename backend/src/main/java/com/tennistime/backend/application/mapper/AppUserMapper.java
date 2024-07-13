package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.AppUserDTO;
import com.tennistime.backend.domain.model.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserDTO toDTO(AppUser appUser);
    AppUser toEntity(AppUserDTO appUserDTO);
}
