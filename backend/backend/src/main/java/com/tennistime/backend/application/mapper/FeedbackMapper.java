package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.FeedbackDTO;
import com.tennistime.backend.domain.model.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    @Mapping(source = "club.id", target = "clubId")
    @Mapping(source = "court.id", target = "courtId")
    FeedbackDTO toDTO(Feedback feedback);

    @Mapping(source = "clubId", target = "club.id")
    @Mapping(source = "courtId", target = "court.id")
    Feedback toEntity(FeedbackDTO feedbackDTO);
}
