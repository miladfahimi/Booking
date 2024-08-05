package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.UserSubscriptionDTO;
import com.tennistime.backend.domain.model.UserSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserSubscriptionMapper {

    @Mapping(target = "startDatePersian", ignore = true)
    @Mapping(target = "endDatePersian", ignore = true)
    UserSubscriptionDTO toDTO(UserSubscription userSubscription);

    @Mapping(target = "startDatePersian", ignore = true)
    @Mapping(target = "endDatePersian", ignore = true)
    UserSubscription toEntity(UserSubscriptionDTO userSubscriptionDTO);
}
