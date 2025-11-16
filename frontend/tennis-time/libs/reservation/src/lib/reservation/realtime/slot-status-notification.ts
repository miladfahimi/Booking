import { ReservationStatus } from '../types';

export interface SlotStatusNotification {
  slotId: string;
  compositeSlotId: string;
  serviceId: string;
  status: ReservationStatus;
  reservationDate?: string | null;
}
