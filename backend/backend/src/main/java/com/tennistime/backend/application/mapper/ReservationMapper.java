package com.tennistime.backend.application.mapper;


import com.tennistime.backend.application.dto.ReservationDTO;
import com.tennistime.backend.domain.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    // @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "court.id", target = "courtId")
    @Mapping(target = "reservationDatePersian", ignore = true) // Exclude from MapStruct mapping
    ReservationDTO toDTO(Reservation reservation);

    // @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "courtId", target = "court.id")
    @Mapping(target = "reservationDatePersian", ignore = true) // Exclude from MapStruct mapping
    Reservation toEntity(ReservationDTO reservationDTO);
}
