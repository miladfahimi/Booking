package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.userDetails.UserProfileDTO;
import com.tennistime.backend.domain.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "dateOfBirthPersian", ignore = true) // Exclude from MapStruct mapping
    UserProfileDTO toDTO(UserProfile userProfile);

    @Mapping(target = "dateOfBirthPersian", ignore = true) // Exclude from MapStruct mapping
    UserProfile toEntity(UserProfileDTO userProfileDTO);
}
