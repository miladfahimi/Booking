package com.tennistime.profile.application.mapper;

import com.tennistime.profile.application.dto.UserProfileDTO;
import com.tennistime.profile.domain.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "dateOfBirthPersian", ignore = true) // Exclude from MapStruct mapping
    UserProfileDTO toDTO(UserProfile userProfile);

    @Mapping(target = "dateOfBirthPersian", ignore = true) // Exclude from MapStruct mapping
    UserProfile toEntity(UserProfileDTO userProfileDTO);
}
