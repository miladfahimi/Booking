package com.tennistime.authentication.application.mapper;


import com.tennistime.authentication.application.dto.AppUserDTO;
import com.tennistime.authentication.domain.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    @Mapping(target = "password", ignore = true)
    AppUserDTO toDTO(AppUser appUser);
    AppUser toEntity(AppUserDTO appUserDTO);
}
