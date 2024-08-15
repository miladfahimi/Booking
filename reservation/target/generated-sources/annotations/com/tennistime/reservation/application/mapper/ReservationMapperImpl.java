package com.tennistime.reservation.application.mapper;

import com.tennistime.reservation.application.dto.ReservationDTO;
import com.tennistime.reservation.domain.model.Reservation;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-15T20:40:06+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public ReservationDTO toDTO(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }

        ReservationDTO reservationDTO = new ReservationDTO();

        reservationDTO.setId( reservation.getId() );
        reservationDTO.setReservationDate( reservation.getReservationDate() );
        reservationDTO.setStartTime( reservation.getStartTime() );
        reservationDTO.setEndTime( reservation.getEndTime() );
        reservationDTO.setStatus( reservation.getStatus() );
        reservationDTO.setUserId( reservation.getUserId() );
        reservationDTO.setServiceId( reservation.getServiceId() );

        return reservationDTO;
    }

    @Override
    public Reservation toEntity(ReservationDTO reservationDTO) {
        if ( reservationDTO == null ) {
            return null;
        }

        Reservation reservation = new Reservation();

        reservation.setId( reservationDTO.getId() );
        reservation.setUserId( reservationDTO.getUserId() );
        reservation.setServiceId( reservationDTO.getServiceId() );
        reservation.setReservationDate( reservationDTO.getReservationDate() );
        reservation.setStartTime( reservationDTO.getStartTime() );
        reservation.setEndTime( reservationDTO.getEndTime() );
        reservation.setStatus( reservationDTO.getStatus() );

        return reservation;
    }
}
