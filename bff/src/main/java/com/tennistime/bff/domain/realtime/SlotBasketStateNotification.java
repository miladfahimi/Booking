package com.tennistime.bff.domain.realtime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Snapshot of basket participation included in slot status notifications.
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
