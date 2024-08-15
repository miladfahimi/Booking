package com.tennistime.reservation.application.mapper;

import com.tennistime.reservation.application.dto.ServiceDTO;
import com.tennistime.reservation.domain.model.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    @Mapping(source = "provider.id", target = "providerId")
    ServiceDTO toDTO(Service service);

    @Mapping(source = "providerId", target = "provider.id")
    Service toEntity(ServiceDTO serviceDTO);
}
