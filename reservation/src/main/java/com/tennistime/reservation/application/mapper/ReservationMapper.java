package com.tennistime.reservation.application.mapper;

import com.tennistime.reservation.application.dto.ReservationDTO;
import com.tennistime.reservation.domain.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "reservationDatePersian", ignore = true)
    ReservationDTO toDTO(Reservation reservation);

    @Mapping(target = "reservationDatePersian", ignore = true)
    Reservation toEntity(ReservationDTO reservationDTO);
}
