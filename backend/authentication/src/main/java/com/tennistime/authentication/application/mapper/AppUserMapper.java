package com.tennistime.authentication.application.mapper;


import com.tennistime.authentication.application.dto.UserDTO;
import com.tennistime.authentication.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    @Mapping(target = "password", ignore = true)
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
}
