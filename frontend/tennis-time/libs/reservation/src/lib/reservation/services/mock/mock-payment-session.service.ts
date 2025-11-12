import { Inject, Injectable, Optional } from '@angular/core';
import { ReservationSummary } from '../../types/reservation-basket.types';
import { PaymentInitiationResult } from '../../types/reservation-payment.types';
import { RESERVATION_FEATURE_CONFIG, ReservationFeatureConfig, defaultReservationFeatureConfig } from '../../config/reservation-feature-config.token';

export interface MockPaymentSessionData {
  paymentId: string;
  referenceNumber: string | null;
  redirectUrl: string;
  providerRedirectUrl: string | null;
  total: number;
  reservations: ReservationSummary[];
  reservationDate: string;
}

@Injectable()
export class MockPaymentSessionService {
  private readonly storageKey = 'reservation-mock-payment';
  private readonly config: ReservationFeatureConfig;

  constructor(
    @Optional() @Inject(RESERVATION_FEATURE_CONFIG) config?: ReservationFeatureConfig
  ) {
    this.config = config ?? defaultReservationFeatureConfig;
  }

  isEnabled(): boolean {
    return this.config.useMockPaymentGateway;
  }

  beginSession(params: { payment: PaymentInitiationResult; reservations: ReservationSummary[]; total: number }): PaymentInitiationResult {
    if (!this.isEnabled() || !params.reservations.length) {
      return params.payment;
    }

    const paymentId = params.payment.paymentId || 'mock-payment';
    const redirectUrl = this.createMockRedirectUrl(paymentId);
    const referenceNumber = params.payment.referenceNumber ?? null;
    const providerRedirectUrl = params.payment.redirectUrl?.trim()?.length ? params.payment.redirectUrl : null;
    const reservationDate = params.reservations[0].reservationDate;

    const sessionPayload: MockPaymentSessionData = {
      paymentId,
      referenceNumber,
      redirectUrl,
      providerRedirectUrl,
      total: params.total,
      reservations: params.reservations,
      reservationDate
    };

    const storage = this.getStorage();
    if (storage) {
      storage.setItem(this.storageKey, JSON.stringify(sessionPayload));
    }

    return { ...params.payment, paymentId, redirectUrl };
  }

  loadSession(): MockPaymentSessionData | null {
    if (!this.isEnabled()) {
      return null;
    }

    const storage = this.getStorage();
    if (!storage) {
      return null;
    }

    const raw = storage.getItem(this.storageKey);
    if (!raw) {
      return null;
    }

    try {
      return JSON.parse(raw) as MockPaymentSessionData;
    } catch {
      return null;
    }
  }

  clear(): void {
    const storage = this.getStorage();
    if (!storage) {
      return;
    }

    storage.removeItem(this.storageKey);
  }

  private createMockRedirectUrl(paymentId: string): string {
    const safeId = encodeURIComponent(paymentId);
    return `/reservation/payment/mock/${safeId}`;
  }

  private getStorage(): Storage | null {
    if (typeof window === 'undefined') {
      return null;
    }

    try {
      return window.localStorage;
    } catch {
      return null;
    }
  }
}