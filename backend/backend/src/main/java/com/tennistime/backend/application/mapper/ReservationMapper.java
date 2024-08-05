package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.domain.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "reservationDatePersian", ignore = true)
    ReservationDTO toDTO(Reservation reservation);

    @Mapping(target = "reservationDatePersian", ignore = true)
    Reservation toEntity(ReservationDTO reservationDTO);
}
