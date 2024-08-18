import { createReducer, on } from '@ngrx/store';
import * as ReservationActions from './reservation.actions';
import { LoadingStatus, UserReservationDTO } from '../types';

export interface ReservationState {
  reservation: UserReservationDTO | null;
  loadingStatus: LoadingStatus;
  error: any;
}

export const initialState: ReservationState = {
  reservation: null,
  loadingStatus: { loading: false, loaded: false },
  error: null,
};

export const reservationReducer = createReducer(
  initialState,
  on(ReservationActions.loadReservation, (state) => ({
    ...state,
    loadingStatus: { loading: true, loaded: false },
    error: null,
  })),
  on(ReservationActions.loadReservationSuccess, (state, { reservation }) => ({
    ...state,
    reservation,
    loadingStatus: { loading: false, loaded: true },
    error: null,
  })),
  on(ReservationActions.loadReservationFailure, (state, { error }) => ({
    ...state,
    error,
    loadingStatus: { loading: false, loaded: true },
  }))
);
