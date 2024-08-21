import { createAction, props } from '@ngrx/store';
import { ServiceDTO } from '../types';

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
