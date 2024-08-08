package com.tennistime.profile.application.mapper;


import com.tennistime.profile.application.dto.UserBookingHistoryDTO;
import com.tennistime.profile.domain.model.UserBookingHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserBookingHistoryMapper {

    @Mapping(target = "bookingDatePersian", ignore = true)
    UserBookingHistoryDTO toDTO(UserBookingHistory userBookingHistory);

    @Mapping(target = "bookingDatePersian", ignore = true)
    UserBookingHistory toEntity(UserBookingHistoryDTO userBookingHistoryDTO);
}
