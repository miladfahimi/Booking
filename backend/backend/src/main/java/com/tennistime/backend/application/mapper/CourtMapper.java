package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.CourtDTO;
import com.tennistime.backend.domain.model.Court;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourtMapper {

    @Mapping(source = "provider.id", target = "providerId")
    CourtDTO toDTO(Court court);

    @Mapping(source = "providerId", target = "provider.id")
    Court toEntity(CourtDTO courtDTO);
}
