package com.tennistime.provider.application.mapper;

import com.tennistime.provider.application.dto.ProviderDTO;
import com.tennistime.provider.domain.model.Provider;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ServiceMapper.class})
public interface ProviderMapper {
    ProviderDTO toDTO(Provider provider);
    Provider toEntity(ProviderDTO providerDTO);
}
