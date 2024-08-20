package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.ServiceDTO;
import com.tennistime.backend.domain.model.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    @Mapping(source = "provider.id", target = "providerId")
    @Mapping(target = "tags", ignore = true)
    ServiceDTO toDTO(Service service);

    @Mapping(source = "providerId", target = "provider.id")
    @Mapping(target = "tags", ignore = true)
    Service toEntity(ServiceDTO serviceDTO);
}
