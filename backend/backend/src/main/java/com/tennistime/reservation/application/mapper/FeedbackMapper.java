package com.tennistime.reservation.application.mapper;

import com.tennistime.reservation.application.dto.FeedbackDTO;
import com.tennistime.reservation.domain.model.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    @Mapping(source = "provider.id", target = "providerId")
    @Mapping(source = "service.id", target = "serviceId")
    FeedbackDTO toDTO(Feedback feedback);

    @Mapping(source = "providerId", target = "provider.id")
    @Mapping(source = "serviceId", target = "service.id")
    Feedback toEntity(FeedbackDTO feedbackDTO);
}
