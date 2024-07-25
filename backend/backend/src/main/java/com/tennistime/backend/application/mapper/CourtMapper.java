package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.CourtDTO;
import com.tennistime.backend.domain.model.Court;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourtMapper {

    @Mapping(source = "club.id", target = "clubId")
    CourtDTO toDTO(Court court);

    @Mapping(source = "clubId", target = "club.id")
    Court toEntity(CourtDTO courtDTO);
}
