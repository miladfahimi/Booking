import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ReservationState } from './reservation.reducer';

export const selectReservationState = createFeatureSelector<ReservationState>('reservation');

export const selectSlots = createSelector(
  selectReservationState,
  (state: ReservationState) => state.slots
);

export const selectSlotsLoading = createSelector(
  selectReservationState,
  (state: ReservationState) => state.loadingStatus.loading
);

export const selectSlotsLoaded = createSelector(
  selectReservationState,
  (state: ReservationState) => state.loadingStatus.loaded
);

export const selectSlotsError = createSelector(
  selectReservationState,
  (state: ReservationState) => state.error
);
