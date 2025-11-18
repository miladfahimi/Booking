package com.tennistime.bff.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Describes basket activity for a slot including ownership details and participation counts.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotBasketStateDTO {

    /**
     * Indicates whether the current user has this slot in the basket.
     */
    private boolean inBasketByCurrentUser;

    /**
     * Indicates whether other users have the slot in their baskets.
     */
    private boolean inBasketByOtherUsers;

    /**
     * Total number of distinct basket entries currently referencing the slot.
     */
    private int totalBasketUsers;
}
