import { InjectionToken } from '@angular/core';

export interface PaymentFeatureConfig {
  useMockPaymentGateway: boolean;
}

export const defaultPaymentFeatureConfig: PaymentFeatureConfig = {
  useMockPaymentGateway: false
};

export const PAYMENT_FEATURE_CONFIG = new InjectionToken<PaymentFeatureConfig>('PAYMENT_FEATURE_CONFIG');
