import { createAction, props } from '@ngrx/store';

export const loadSlots = createAction(
  '[Reservation] Load Slots',
  props<{ serviceId: string, date: string }>()
);

export const loadSlotsSuccess = createAction(
  '[Reservation] Load Slots Success',
  props<{ slots: string[] }>()
);

export const loadSlotsFailure = createAction(
  '[Reservation] Load Slots Failure',
  props<{ error: any }>()
);
