import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ReservationState } from './reservation.reducer';
import { ServiceDTO } from '../types';

export const selectReservationState = createFeatureSelector<ReservationState>('reservation');

// Selector to get the entire ServiceDTO
export const selectService = createSelector(
  selectReservationState,
  (state: ReservationState) => state.service
);

// Selector to get the slots array from the ServiceDTO
export const selectSlots = createSelector(
  selectService,
  (service: ServiceDTO | null) => service ? service.slots : null
);

// Selector to get the slot count from the ServiceDTO
export const selectSlotCount = createSelector(
  selectService,
  (service: ServiceDTO | null) => service ? service.slots.length : 0
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
