import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ReservationState } from './reservation.reducer';

export const selectReservationState = createFeatureSelector<ReservationState>('reservation');

// Selector to get user reservation data
export const selectReservation = createSelector(
  selectReservationState,
  (state: ReservationState) => state.reservation
);
export const selectReservationLoading = createSelector(
  selectReservationState,
  (state: ReservationState) => state.loadingStatus.loading
);

export const selectReservationError = createSelector(
  selectReservationState,
  (state: ReservationState) => state.error
);
