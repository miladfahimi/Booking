import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, mergeMap } from 'rxjs/operators';
import * as ReservationActions from './reservation.actions';
import { ReservationService } from '../services/reservation.service';
import { UserReservationDTO } from '../types';

@Injectable()
export class ReservationEffects {
  constructor(
    private actions$: Actions,
    private reservationService: ReservationService
  ) {}

  loadReservation$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ReservationActions.loadReservation),
      mergeMap((action) =>
        this.reservationService.getReservationById(action.userId).pipe(
          map((reservation: UserReservationDTO) =>
            ReservationActions.loadReservationSuccess({ reservation })
          ),
          catchError((error) =>
            of(ReservationActions.loadReservationFailure({ error }))
          )
        )
      )
    )
  );
}
