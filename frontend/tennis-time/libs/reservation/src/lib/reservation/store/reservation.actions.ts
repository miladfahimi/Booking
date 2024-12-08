import { createAction, props } from '@ngrx/store';
import {ProviderDTO, ServiceDTO} from '../types';

export const loadSlots = createAction(
  '[Reservation] Load Slots',
  props<{ serviceId: string, date: string }>()
);

export const loadSlotsSuccess = createAction(
  '[Reservation] Load Slots Success',
  props<{ service: ServiceDTO }>() // Updated to expect ServiceDTO
);

export const loadSlotsFailure = createAction(
  '[Reservation] Load Slots Failure',
  props<{ error: any }>()
);

export const loadProvidersWithServices = createAction(
  '[Reservation] Load Providers with Services'
);

export const loadProvidersWithServicesSuccess = createAction(
  '[Reservation] Load Providers with Services Success',
  props<{ providers: ProviderDTO[] }>() // Array of ProviderDTO objects
);

export const loadProvidersWithServicesFailure = createAction(
  '[Reservation] Load Providers with Services Failure',
  props<{ error: any }>()
);
