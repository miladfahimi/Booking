package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.AppUserDTO;
import com.tennistime.backend.domain.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    @Mapping(target = "password", ignore = true)
    AppUserDTO toDTO(AppUser appUser);
    AppUser toEntity(AppUserDTO appUserDTO);
}
