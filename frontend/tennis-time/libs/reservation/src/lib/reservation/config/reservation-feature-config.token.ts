import { InjectionToken } from '@angular/core';

export interface ReservationFeatureConfig {
  useMockPaymentGateway: boolean;
}

export const RESERVATION_FEATURE_CONFIG = new InjectionToken<ReservationFeatureConfig>('RESERVATION_FEATURE_CONFIG');

export const defaultReservationFeatureConfig: ReservationFeatureConfig = {
  useMockPaymentGateway: false
};
