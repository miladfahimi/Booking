import { ReservationStatus } from '../types';

export interface ReservationBasketItem {
  id?: string;
  slotId: string;
  serviceId: string;
  providerId: string;
  serviceName: string;
  reservationDate: string;
  reservationDatePersian: string;
  startTime: string;
  endTime: string;
  userId: string;
  price: number;
  durationMinutes: number;
  status?: ReservationStatus;
}

export interface ReservationBulkRequestItem {
  reservationDate: string;
  reservationDatePersian: string;
  startTime: string;
  endTime: string;
  status: ReservationStatus;
  userId: string;
  providerId: string;
  serviceId: string;
}

export interface ReservationSummary {
  id: string;
  reservationDate: string;
  reservationDatePersian: string | null;
  startTime: string;
  endTime: string;
  status: ReservationStatus;
  userId: string;
  providerId: string;
  serviceId: string;
}
