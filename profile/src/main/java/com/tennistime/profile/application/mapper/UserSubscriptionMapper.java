package com.tennistime.profile.application.mapper;

import com.tennistime.profile.application.dto.UserSubscriptionDTO;
import com.tennistime.profile.domain.model.UserSubscription;
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
