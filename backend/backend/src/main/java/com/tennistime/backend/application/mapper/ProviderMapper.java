package com.tennistime.backend.application.mapper;

import com.tennistime.backend.application.dto.ProviderDTO;
import com.tennistime.backend.domain.model.Provider;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ServiceMapper.class})
public interface ProviderMapper {
    ProviderDTO toDTO(Provider provider);
    Provider toEntity(ProviderDTO providerDTO);
}
