import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, mergeMap } from 'rxjs/operators';
import * as ReservationActions from './reservation.actions';
import { ReservationService } from '../services/reservation.service';
import { ServiceDTO } from '../types';

@Injectable()
export class ReservationEffects {
  constructor(
    private actions$: Actions,
    private reservationService: ReservationService
  ) {}

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
}
