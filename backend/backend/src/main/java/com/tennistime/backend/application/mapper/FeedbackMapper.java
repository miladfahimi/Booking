package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.FeedbackDTO;
import com.tennistime.backend.domain.model.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    @Mapping(source = "provider.id", target = "providerId")
    @Mapping(source = "court.id", target = "courtId")
    FeedbackDTO toDTO(Feedback feedback);

    @Mapping(source = "providerId", target = "provider.id")
    @Mapping(source = "courtId", target = "court.id")
    Feedback toEntity(FeedbackDTO feedbackDTO);
}
