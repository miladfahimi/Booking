package com.tennistime.reservation.domain.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Describes the basket composition for a slot at the time a notification is emitted.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotBasketStateNotification {


    private int inBasketByCurrentUser;
    private int inBasketByOtherUsers;
    private int totalBasketUsers;
}
