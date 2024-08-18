import { createAction, props } from '@ngrx/store';
import { UserReservationDTO } from '../types';

export const loadReservation = createAction(
  '[Reservation] Load Reservation',
  props<{ userId: string }>()
);

export const loadReservationSuccess = createAction(
  '[Reservation] Load Reservation Success',
  props<{ reservation: UserReservationDTO }>()
);

export const loadReservationFailure = createAction(
  '[Reservation] Load Reservation Failure',
  props<{ error: any }>()
);
