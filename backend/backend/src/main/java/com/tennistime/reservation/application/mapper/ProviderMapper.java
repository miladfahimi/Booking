package com.tennistime.reservation.application.mapper;

import com.tennistime.reservation.application.dto.ProviderDTO;
import com.tennistime.reservation.domain.model.Provider;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ServiceMapper.class})
public interface ProviderMapper {
    ProviderDTO toDTO(Provider provider);
    Provider toEntity(ProviderDTO providerDTO);
}
