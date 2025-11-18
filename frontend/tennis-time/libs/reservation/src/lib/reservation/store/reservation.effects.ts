import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store, Action } from '@ngrx/store';
import { from, of } from 'rxjs';
import { catchError, map, mergeMap, withLatestFrom } from 'rxjs/operators';
import * as ReservationActions from './reservation.actions';
import { ReservationService } from '../services/reservation.service';
import { ProviderDTO, ReservationStatus, ServiceDTO } from '../types';
import { selectBasket } from './reservation.selectors';
import { ReservationCheckoutService } from '../services/reservation-checkout.service';
import { ReservationBasketApiService } from '../services/reservation-basket-api.service';
import { MockPaymentSessionService } from '../services/mock/mock-payment-session.service';
import { SlotStatusRealtimeService } from '../realtime/slot-status-realtime.service';
import { CoreAuthService } from '@tennis-time/core';

@Injectable()
export class ReservationEffects {
  constructor(
    private actions$: Actions,
    private reservationService: ReservationService,
    private reservationCheckoutService: ReservationCheckoutService,
    private reservationBasketApiService: ReservationBasketApiService,
    private mockPaymentSession: MockPaymentSessionService,
    private store: Store,
    private slotStatusRealtime: SlotStatusRealtimeService,
    private coreAuthService: CoreAuthService
  ) {
    this.slotStatusRealtime.connect();
  }

  slotStatusUpdates$ = createEffect(() =>
    this.slotStatusRealtime.updates$.pipe(
      mergeMap(notification => {
        const currentUserId = this.coreAuthService.getUserId();
        const actions: Action[] = [
          ReservationActions.slotStatusUpdated({ notification, currentUserId })
        ];
        const isForeignBasketUpdate =
          notification.status === ReservationStatus.IN_BASKET &&
          notification.userId &&
          (!currentUserId || notification.userId !== currentUserId);
        if (isForeignBasketUpdate) {
          actions.push(ReservationActions.slotHeldByAnotherUser({ notification }));
        }
        return from(actions);
      })
    )
  );

  loadSlots$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ReservationActions.loadSlots),
      mergeMap(action => {
        const currentUserId = this.coreAuthService.getUserId();
        return this.reservationService.getSlotsByDate(action.date, currentUserId).pipe(
          map((services: ServiceDTO[]) =>
            ReservationActions.loadSlotsSuccess({ services })
          ),
          catchError(error =>
            of(ReservationActions.loadSlotsFailure({ error }))
          )
        );
      })
    )
  );

  loadProvidersWithServices$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ReservationActions.loadProvidersWithServices),
      mergeMap(() =>
        this.reservationService.getProvidersWithServices().pipe(
          map((providers: ProviderDTO[]) =>
            ReservationActions.loadProvidersWithServicesSuccess({ providers })
          ),
          catchError(error =>
            of(ReservationActions.loadProvidersWithServicesFailure({ error }))
          )
        )
      )
    )
  );

  loadBasket$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ReservationActions.loadBasket),
      mergeMap(({ userId }) =>
        this.reservationBasketApiService.getBasket(userId).pipe(
          map(items => ReservationActions.loadBasketSuccess({ items })),
          catchError(error => of(ReservationActions.loadBasketFailure({ error })))
        )
      )
    )
  );

  addSlotToBasket$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ReservationActions.addSlotToBasket),
      mergeMap(({ item }) =>
        this.reservationBasketApiService.addItem(item).pipe(
          map(savedItem => ReservationActions.addSlotToBasketSuccess({ item: savedItem })),
          catchError(error => of(ReservationActions.addSlotToBasketFailure({ slotId: item.slotId, error })))
        )
      )
    )
  );

  removeSlotFromBasket$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ReservationActions.removeSlotFromBasket),
      mergeMap(({ userId, slotId }) =>
        this.reservationBasketApiService.removeItem(userId, slotId).pipe(
          map(() => ReservationActions.removeSlotFromBasketSuccess({ slotId })),
          catchError(error => of(ReservationActions.removeSlotFromBasketFailure({ slotId, error })))
        )
      )
    )
  );

  clearBasket$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ReservationActions.clearBasket),
      mergeMap(({ userId }) =>
        this.reservationBasketApiService.clear(userId).pipe(
          map(() => ReservationActions.clearBasketSuccess()),
          catchError(error => of(ReservationActions.clearBasketFailure({ error })))
        )
      )
    )
  );

  checkoutBasket$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ReservationActions.checkoutBasket),
      withLatestFrom(this.store.select(selectBasket)),
      mergeMap(([action, basket]) => {
        if (!basket.length) {
          return of(ReservationActions.checkoutBasketFailure({ error: null }));
        }

        return this.reservationCheckoutService.checkout(basket).pipe(
          mergeMap(result => {
            const totalAmount = basket.reduce((total, item) => total + (item.price || 0), 0);
            let payment = result.payment;

            if (this.mockPaymentSession.isEnabled()) {
              payment = this.mockPaymentSession.beginSession({
                payment,
                reservations: result.reservations,
                total: totalAmount
              });
            }

            return [
              ReservationActions.checkoutBasketSuccess({ reservations: result.reservations, payment }),
              ReservationActions.loadSlots({ date: action.date })
            ];
          }),
          catchError(error =>
            of(ReservationActions.checkoutBasketFailure({ error }))
          )
        );
      })
    )
  );

  completeMockPayment$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ReservationActions.completeMockPayment),
      mergeMap(() => {
        if (!this.mockPaymentSession.isEnabled()) {
          return of(ReservationActions.completeMockPaymentFailure({ error: 'MOCK_DISABLED' }));
        }

        const session = this.mockPaymentSession.loadSession();
        if (!session || !session.reservations.length) {
          return of(ReservationActions.completeMockPaymentFailure({ error: 'NO_SESSION' }));
        }

        return this.reservationCheckoutService.finalizeReservations(session.reservations).pipe(
          mergeMap(() => {
            this.mockPaymentSession.clear();
            const actions: Action[] = [
              ReservationActions.completeMockPaymentSuccess(),
              ReservationActions.loadSlots({ date: session.reservationDate })
            ];

            return from(actions);
          }),
          catchError(error => of(ReservationActions.completeMockPaymentFailure({ error })))
        );
      })
    )
  );
}
