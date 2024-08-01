package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.UserProfileDTO;
import com.tennistime.backend.domain.model.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileDTO toDTO(UserProfile userProfile);
    UserProfile toEntity(UserProfileDTO userProfileDTO);
}
