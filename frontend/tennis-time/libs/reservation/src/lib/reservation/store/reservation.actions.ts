import { createAction, props } from '@ngrx/store';
import { ProviderDTO, ServiceDTO } from '../types';
import { ReservationBasketItem, ReservationSummary } from '../types/reservation-basket.types';
import { PaymentInitiationResult } from '../types/reservation-payment.types';

// export const loadSlots = createAction(
//   '[Reservation] Load Slots',
//   props<{ serviceId: string, date: string }>()
// );

// export const loadSlotsSuccess = createAction(
//   '[Reservation] Load Slots Success',
//   props<{ service: ServiceDTO }>() // Updated to expect ServiceDTO
// );

export const loadSlots = createAction(
  '[Reservation] Load Slots',
  props<{ date: string }>()
);

export const loadSlotsSuccess = createAction(
  '[Reservation] Load Slots Success',
  props<{ services: ServiceDTO[] }>()
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

export const addSlotToBasket = createAction(
  '[Reservation] Add Slot To Basket',
  props<{ item: ReservationBasketItem }>()
);

export const removeSlotFromBasket = createAction(
  '[Reservation] Remove Slot From Basket',
  props<{ slotId: string }>()
);

export const clearBasket = createAction(
  '[Reservation] Clear Basket'
);

export const checkoutBasket = createAction(
  '[Reservation] Checkout Basket',
  props<{ date: string }>()
);

export const checkoutBasketSuccess = createAction(
  '[Reservation] Checkout Basket Success',
  props<{ reservations: ReservationSummary[]; payment: PaymentInitiationResult }>()
);

export const checkoutBasketFailure = createAction(
  '[Reservation] Checkout Basket Failure',
  props<{ error: any }>()
);