package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.ClubDTO;
import com.tennistime.backend.domain.model.Club;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CourtMapper.class})
public interface ClubMapper {
    ClubDTO toDTO(Club club);
    Club toEntity(ClubDTO clubDTO);
}
