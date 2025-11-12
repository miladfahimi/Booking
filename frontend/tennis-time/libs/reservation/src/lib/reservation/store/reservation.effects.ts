import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { of } from 'rxjs';
import { catchError, map, mergeMap, withLatestFrom } from 'rxjs/operators';
import * as ReservationActions from './reservation.actions';
import { ReservationService } from '../services/reservation.service';
import { ServiceDTO, ProviderDTO } from '../types';
import { selectBasket } from './reservation.selectors';
import { ReservationCheckoutService } from '../services/reservation-checkout.service';

@Injectable()
export class ReservationEffects {
  constructor(
    private actions$: Actions,
    private reservationService: ReservationService,
    private reservationCheckoutService: ReservationCheckoutService,
    private store: Store
  ) { }

  // Effect for loading slots
  loadSlots$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ReservationActions.loadSlots),
      mergeMap(action =>
        this.reservationService.getSlotsByDate(action.date).pipe(
          map((services: ServiceDTO[]) =>
            ReservationActions.loadSlotsSuccess({ services })
          ),
          catchError(error =>
            of(ReservationActions.loadSlotsFailure({ error }))
          )
        )
      )
    )
  );

  // Effect for loading providers with services
  loadProvidersWithServices$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ReservationActions.loadProvidersWithServices),
      mergeMap(() =>
        this.reservationService.getProvidersWithServices().pipe(
          map((providers: ProviderDTO[]) =>
            ReservationActions.loadProvidersWithServicesSuccess({ providers })  // Pass the array of ProviderDTO objects
          ),
          catchError(error =>
            of(ReservationActions.loadProvidersWithServicesFailure({ error }))
          )
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
          mergeMap(result => [
            ReservationActions.checkoutBasketSuccess({ reservations: result.reservations, payment: result.payment }),
            ReservationActions.loadSlots({ date: action.date })
          ]),
          catchError(error =>
            of(ReservationActions.checkoutBasketFailure({ error }))
          )
        );
      })
    )
  );
}