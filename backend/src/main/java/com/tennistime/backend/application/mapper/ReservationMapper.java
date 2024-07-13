package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.domain.model.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    ReservationDTO toDTO(Reservation reservation);
    Reservation toEntity(ReservationDTO reservationDTO);
}
