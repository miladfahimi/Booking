import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ReservationState } from './reservation.reducer';
import { ServiceDTO, ProviderDTO } from '../types';

// Selector to get the entire ReservationState
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

// Selector to get the loading status for slots
export const selectSlotsLoading = createSelector(
  selectReservationState,
  (state: ReservationState) => state.loadingStatus.loading
);

// Selector to get the loaded status for slots
export const selectSlotsLoaded = createSelector(
  selectReservationState,
  (state: ReservationState) => state.loadingStatus.loaded
);

// Selector to get the error for slots
export const selectSlotsError = createSelector(
  selectReservationState,
  (state: ReservationState) => state.error
);

// Selector to get the list of providers
export const selectProviders = createSelector(
  selectReservationState,
  (state: ReservationState) => state.providers
);

// Selector to get the loading status for providers
export const selectProvidersLoading = createSelector(
  selectReservationState,
  (state: ReservationState) => state.providersLoadingStatus.loading
);

// Selector to get the loaded status for providers
export const selectProvidersLoaded = createSelector(
  selectReservationState,
  (state: ReservationState) => state.providersLoadingStatus.loaded
);
