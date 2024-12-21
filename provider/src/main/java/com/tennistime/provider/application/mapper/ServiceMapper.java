package com.tennistime.provider.application.mapper;

import com.tennistime.provider.application.dto.ServiceDTO;
import com.tennistime.provider.domain.model.Service;
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
