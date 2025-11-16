import { createAction, props } from '@ngrx/store';
import { ProviderDTO, ServiceDTO } from '../types';
import { SlotStatusNotification } from '../realtime/slot-status-notification';
import { ReservationBasketItem, ReservationSummary } from '../types/reservation-basket.types';
import { PaymentInitiationResult } from '../types/reservation-payment.types';

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

export const addSlotToBasketSuccess = createAction(
  '[Reservation] Add Slot To Basket Success',
  props<{ item: ReservationBasketItem }>()
);

export const addSlotToBasketFailure = createAction(
  '[Reservation] Add Slot To Basket Failure',
  props<{ slotId: string; error: any }>()
);

export const removeSlotFromBasket = createAction(
  '[Reservation] Remove Slot From Basket',
  props<{ userId: string; slotId: string }>()
);

export const removeSlotFromBasketSuccess = createAction(
  '[Reservation] Remove Slot From Basket Success',
  props<{ slotId: string }>()
);

export const removeSlotFromBasketFailure = createAction(
  '[Reservation] Remove Slot From Basket Failure',
  props<{ slotId: string; error: any }>()
);

export const clearBasket = createAction(
  '[Reservation] Clear Basket',
  props<{ userId: string }>()
);

export const clearBasketSuccess = createAction(
  '[Reservation] Clear Basket Success'
);

export const clearBasketFailure = createAction(
  '[Reservation] Clear Basket Failure',
  props<{ error: any }>()
);

export const loadBasket = createAction(
  '[Reservation] Load Basket',
  props<{ userId: string }>()
);

export const loadBasketSuccess = createAction(
  '[Reservation] Load Basket Success',
  props<{ items: ReservationBasketItem[] }>()
);

export const loadBasketFailure = createAction(
  '[Reservation] Load Basket Failure',
  props<{ error: any }>()
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

export const completeMockPayment = createAction(
  '[Reservation] Complete Mock Payment'
);

export const completeMockPaymentSuccess = createAction(
  '[Reservation] Complete Mock Payment Success'
);

export const completeMockPaymentFailure = createAction(
  '[Reservation] Complete Mock Payment Failure',
  props<{ error: any }>()
);

export const slotStatusUpdated = createAction(
  '[Reservation] Slot Status Updated',
  props<{ notification: SlotStatusNotification; currentUserId: string | null }>()
);

export const slotHeldByAnotherUser = createAction(
  '[Reservation] Slot Held By Another User',
  props<{ notification: SlotStatusNotification }>()
);

export const dismissSlotHoldWarning = createAction(
  '[Reservation] Dismiss Slot Hold Warning'
);
