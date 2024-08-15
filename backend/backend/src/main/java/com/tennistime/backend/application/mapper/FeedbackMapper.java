package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.FeedbackDTO;
import com.tennistime.backend.domain.model.Feedback;
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
