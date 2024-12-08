import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, mergeMap } from 'rxjs/operators';
import * as ReservationActions from './reservation.actions';
import { ReservationService } from '../services/reservation.service';
import { ServiceDTO, ProviderDTO } from '../types';

@Injectable()
export class ReservationEffects {
  constructor(
    private actions$: Actions,
    private reservationService: ReservationService
  ) {}

  // Effect for loading slots
  loadSlots$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ReservationActions.loadSlots),
      mergeMap(action =>
        this.reservationService.getSlotsByServiceAndDate(action.serviceId, action.date).pipe(
          map((service: ServiceDTO) =>
            ReservationActions.loadSlotsSuccess({ service })  // Pass the ServiceDTO object
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
}
