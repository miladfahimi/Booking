import { createReducer, on } from '@ngrx/store';
import * as ReservationActions from './reservation.actions';
import { LoadingStatus } from '../types';

export interface ReservationState {
  slots: string[] | null;
  loadingStatus: LoadingStatus;
  error: any;
}

export const initialState: ReservationState = {
  slots: null,
  loadingStatus: { loading: false, loaded: false },
  error: null,
};

export const reservationReducer = createReducer(
  initialState,
  on(ReservationActions.loadSlots, (state) => ({
    ...state,
    loadingStatus: { loading: true, loaded: false },
    error: null,
  })),
  on(ReservationActions.loadSlotsSuccess, (state, { slots }) => ({
    ...state,
    slots,
    loadingStatus: { loading: false, loaded: true },
    error: null,
  })),
  on(ReservationActions.loadSlotsFailure, (state, { error }) => ({
    ...state,
    error,
    loadingStatus: { loading: false, loaded: true },
  }))
);
