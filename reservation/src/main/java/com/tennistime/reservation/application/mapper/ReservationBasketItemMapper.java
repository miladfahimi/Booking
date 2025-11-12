package com.tennistime.reservation.application.mapper;

import com.tennistime.reservation.application.dto.ReservationBasketItemDTO;
import com.tennistime.reservation.domain.model.basket.ReservationBasketItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper bridging {@link ReservationBasketItem} aggregates and their DTO representation.
 */
@Mapper(componentModel = "spring")
public interface ReservationBasketItemMapper {

    /**
     * Converts a basket entity to its DTO form.
     *
     * @param basketItem entity to map
     * @return mapped DTO instance
     */
    ReservationBasketItemDTO toDTO(ReservationBasketItem basketItem);

    /**
     * Converts a basket DTO to its entity form.
     *
     * @param basketItemDTO dto to map
     * @return mapped entity instance
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ReservationBasketItem toEntity(ReservationBasketItemDTO basketItemDTO);

    /**
     * Updates an existing entity with values from the DTO.
     *
     * @param basketItemDTO incoming data
     * @param entity        entity to mutate
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ReservationBasketItemDTO basketItemDTO, @MappingTarget ReservationBasketItem entity);
}
