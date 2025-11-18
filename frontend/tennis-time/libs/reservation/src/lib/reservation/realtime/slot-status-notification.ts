import { ReservationStatus, SlotBasketState } from '../types';

export type SlotBasketStateNotification = Partial<SlotBasketState>;

export interface SlotStatusNotification {
  slotId: string;
  compositeSlotId: string;
  serviceId: string;
  status: ReservationStatus;
  reservationDate?: string | null;
  userId?: string | null;
  basketState?: SlotBasketStateNotification | null;
}