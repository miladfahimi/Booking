import { Inject, Injectable, Optional } from '@angular/core';
import { forkJoin, map, Observable, of, switchMap } from 'rxjs';
import { ReservationCreationService } from './reservation-creation.service';
import { ReservationPaymentService } from './reservation-payment.service';
import { ReservationStatus } from '../types';
import { ReservationBasketItem, ReservationBulkRequestItem, ReservationSummary } from '../types/reservation-basket.types';
import { PaymentInitiationPayload, PaymentInitiationResult } from '../types/reservation-payment.types';
import { RESERVATION_FEATURE_CONFIG, ReservationFeatureConfig, defaultReservationFeatureConfig } from '../config/reservation-feature-config.token';

export interface ReservationCheckoutResult {
  reservations: ReservationSummary[];
  payment: PaymentInitiationResult;
}

@Injectable()
export class ReservationCheckoutService {
  private readonly featureConfig: ReservationFeatureConfig;

  constructor(
    private readonly reservationCreationService: ReservationCreationService,
    private readonly reservationPaymentService: ReservationPaymentService,
    @Optional() @Inject(RESERVATION_FEATURE_CONFIG) featureConfig?: ReservationFeatureConfig
  ) {
    this.featureConfig = featureConfig ?? defaultReservationFeatureConfig;
  }

  checkout(items: ReservationBasketItem[]): Observable<ReservationCheckoutResult> {
    if (!items.length) {
      return of({ reservations: [], payment: { paymentId: '' } });
    }

    const payload = this.mapToBulkPayload(items);
    return this.reservationCreationService.createReservationsBulk(payload).pipe(
      switchMap(reservations => this.handlePayment(reservations, items))
    );
  }

  private mapToBulkPayload(items: ReservationBasketItem[]): ReservationBulkRequestItem[] {
    return items.map(item => ({
      reservationDate: item.reservationDate,
      reservationDatePersian: item.reservationDatePersian,
      startTime: item.startTime,
      endTime: item.endTime,
      status: ReservationStatus.PENDING,
      userId: item.userId,
      providerId: item.providerId,
      serviceId: item.serviceId,
      slotId: item.slotId
    }));
  }

  private handlePayment(reservations: ReservationSummary[], items: ReservationBasketItem[]): Observable<ReservationCheckoutResult> {
    if (!reservations.length) {
      return of({ reservations, payment: { paymentId: '' } });
    }

    const amount = this.calculateTotal(items);
    const primaryReservationId = reservations[0].id;
    const paymentPayload: PaymentInitiationPayload = {
      reservationId: primaryReservationId,
      amount
    };

    return this.reservationPaymentService.initiatePayment(paymentPayload).pipe(
      switchMap(payment => {
        if (this.featureConfig.useMockPaymentGateway) {
          return of({ reservations, payment });
        }

        return this.confirmReservations(reservations).pipe(
          map(() => ({ reservations, payment }))
        );
      })
    );
  }

  private calculateTotal(items: ReservationBasketItem[]): number {
    return items.reduce((total, item) => total + (item.price || 0), 0);
  }

  finalizeReservations(reservations: ReservationSummary[]): Observable<ReservationSummary[]> {
    return this.confirmReservations(reservations);
  }

  private confirmReservations(reservations: ReservationSummary[]): Observable<ReservationSummary[]> {
    if (!reservations.length) {
      return of([]);
    }

    const updates = reservations.map(reservation =>
      this.reservationCreationService.updateReservationStatus(reservation.id, ReservationStatus.CONFIRMED)
    );

    return forkJoin(updates);
  }
}