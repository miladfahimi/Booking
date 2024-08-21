import { createReducer, on } from '@ngrx/store';
import * as ReservationActions from './reservation.actions';
import { LoadingStatus, ServiceDTO } from '../types';  // Import the ServiceDTO interface

export interface ReservationState {
  service: ServiceDTO | null;  // Store the full ServiceDTO object
  loadingStatus: LoadingStatus;
  error: any;
}

export const initialState: ReservationState = {
  service: null,  // Initially, no service data is loaded
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
  on(ReservationActions.loadSlotsSuccess, (state, { service }) => ({
    ...state,
    service,  // Store the entire ServiceDTO object
    loadingStatus: { loading: false, loaded: true },
    error: null,
  })),
  on(ReservationActions.loadSlotsFailure, (state, { error }) => ({
    ...state,
    error,
    loadingStatus: { loading: false, loaded: true },
  }))
);
