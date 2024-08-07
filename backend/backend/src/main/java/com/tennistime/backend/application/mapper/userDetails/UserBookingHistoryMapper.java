package com.tennistime.backend.application.mapper.userDetails;

import com.tennistime.backend.application.dto.userDetails.UserBookingHistoryDTO;
import com.tennistime.backend.domain.model.userDetails.UserBookingHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserBookingHistoryMapper {

    @Mapping(target = "bookingDatePersian", ignore = true)
    UserBookingHistoryDTO toDTO(UserBookingHistory userBookingHistory);

    @Mapping(target = "bookingDatePersian", ignore = true)
    UserBookingHistory toEntity(UserBookingHistoryDTO userBookingHistoryDTO);
}
