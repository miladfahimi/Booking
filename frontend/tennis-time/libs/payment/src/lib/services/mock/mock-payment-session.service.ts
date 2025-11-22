import { Inject, Injectable, Optional } from '@angular/core';
import { PaymentMockSessionData, PaymentInitiationResult, PaymentSessionReservation } from '../../types/payment.types';
import { PAYMENT_FEATURE_CONFIG, PaymentFeatureConfig, defaultPaymentFeatureConfig } from '../../config/payment-feature-config.token';

@Injectable({ providedIn: 'root' })
export class MockPaymentSessionService {
  private readonly storageKey = 'reservation-mock-payment';
  private readonly config: PaymentFeatureConfig;

  constructor(@Optional() @Inject(PAYMENT_FEATURE_CONFIG) config?: PaymentFeatureConfig) {
    this.config = config ?? defaultPaymentFeatureConfig;
  }

  isEnabled(): boolean {
    return this.config.useMockPaymentGateway;
  }

  beginSession(params: { payment: PaymentInitiationResult; reservations: PaymentSessionReservation[]; total: number }): PaymentInitiationResult {
    if (!this.isEnabled() || !params.reservations.length) {
      return params.payment;
    }

    const paymentId = params.payment.paymentId || 'mock-payment';
    const redirectUrl = this.createMockRedirectUrl(paymentId);
    const referenceNumber = params.payment.referenceNumber ?? null;
    const providerRedirectUrl = params.payment.redirectUrl?.trim()?.length ? params.payment.redirectUrl : null;
    const reservationDate = params.reservations[0].reservationDate;

    const sessionPayload: PaymentMockSessionData = {
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

  loadSession(): PaymentMockSessionData | null {
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
      return JSON.parse(raw) as PaymentMockSessionData;
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