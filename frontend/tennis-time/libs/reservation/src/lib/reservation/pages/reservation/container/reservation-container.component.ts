import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { combineLatest, Observable, Subject } from 'rxjs';
import { map, takeUntil } from 'rxjs/operators';
import * as jalaali from 'jalaali-js';
import { CoreAuthService } from '@tennis-time/core';
import { MockPaymentSessionService } from '../../../services/mock/mock-payment-session.service';
import { MockPaymentNavigationService } from '../../../services/mock/mock-payment-navigation.service';

import { addSlotToBasket, checkoutBasket, loadBasket, loadProvidersWithServices, loadSlots, removeSlotFromBasket } from '../../../store/reservation.actions';
import { selectBasket, selectBasketTotal, selectCheckoutLoading, selectPaymentResult, selectProviders, selectSlotsByService, selectSlotsLoading } from '../../../store/reservation.selectors';
import { ProviderDTO, ReservationStatus, ServiceDTO, SlotDTO } from '../../../types';
import { ReservationBasketItem } from '../../../types/reservation-basket.types';
import { PaymentInitiationResult } from '../../../types/reservation-payment.types';
import { TimelineSlotDetails } from './timeline/tileline-slot-modals/timeline-slot-modals.component';

@Component({
  selector: 'app-reservation-container',
  templateUrl: './reservation-container.component.html',
  styleUrls: ['./reservation-container.component.scss']
})
export class ReservationContainerComponent implements OnInit, OnDestroy {
  isMobileView = window.innerWidth <= 768;
  timeSlots$: Observable<Record<string, SlotDTO[]>>;
  loading$: Observable<boolean>;
  basketItems$: Observable<ReservationBasketItem[]>;
  basketTotal$: Observable<number>;
  checkoutLoading$: Observable<boolean>;
  paymentResult$: Observable<PaymentInitiationResult | null>;
  items: ServiceDTO[] = [];
  selectedService: ServiceDTO | null = null;
  selectedDate: Date = new Date();

  private readonly providerId = '11111111-1111-1111-1111-111111111111';
  private readonly destroy$ = new Subject<void>();
  private userId: string | null = null;
  private lastOpenedPaymentId: string | null = null;

  constructor(
    private readonly store: Store,
    private readonly coreAuthService: CoreAuthService,
    private readonly mockPaymentSession: MockPaymentSessionService,
    private readonly mockPaymentNavigation: MockPaymentNavigationService
  ) {
    this.loading$ = this.store.select(selectSlotsLoading);
    this.basketItems$ = this.store.select(selectBasket);
    this.basketTotal$ = this.store.select(selectBasketTotal);
    this.checkoutLoading$ = this.store.select(selectCheckoutLoading);
    this.paymentResult$ = this.store.select(selectPaymentResult);

    this.timeSlots$ = combineLatest([
      this.store.select(selectSlotsByService),
      this.store.select(selectBasket)
    ]).pipe(
      map(([slots, basket]) => this.mergeBasketSlots(slots, basket))
    );

    this.paymentResult$
      .pipe(takeUntil(this.destroy$))
      .subscribe(payment => {
        if (!payment) {
          this.lastOpenedPaymentId = null;
          return;
        }

        if (!this.mockPaymentSession.isEnabled()) {
          return;
        }

        if (!payment.redirectUrl || !payment.paymentId) {
          return;
        }

        if (this.lastOpenedPaymentId === payment.paymentId) {
          return;
        }

        this.lastOpenedPaymentId = payment.paymentId;
        this.mockPaymentNavigation.open(payment.redirectUrl);
      });
  }


  ngOnInit(): void {
    this.userId = this.coreAuthService.getUserId();
    if (this.userId) {
      this.store.dispatch(loadBasket({ userId: this.userId }));
    }

    this.store.dispatch(loadProvidersWithServices());
    this.dispatchLoadSlotsForDate(this.selectedDate);

    this.store.select(selectProviders)
      .pipe(takeUntil(this.destroy$))
      .subscribe((providers: ProviderDTO[] | null) => {
        if (!providers) {
          return;
        }

        const provider = providers.find(p => p.id === this.providerId);
        if (provider && provider.services) {
          this.items = provider.services.map(service => ({
            ...service,
            selected: service.selected || false,
          }));
        }
      });
  }

  onDaySelected(day: any): void {
    this.selectedDate = typeof day.date === 'string' ? this.parseDate(day.date) : new Date(day.date);
    this.dispatchLoadSlotsForDate(this.selectedDate);
  }

  selectDuration(service: ServiceDTO): void {
    this.items.forEach(item => item.selected = false);
    service.selected = true;
    this.selectedService = service;
  }

  onSlotPicked(event: { service: ServiceDTO; slot: SlotDTO }): void {
    if (!event) {
      return;
    }

    this.selectDuration(event.service);
  }

  private parseDate(dateString: string): Date {
    const parts = dateString.split('/');
    const day = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10) - 1;
    const year = parseInt(parts[2], 10);
    return new Date(year, month, day);
  }

  private dispatchLoadSlotsForDate(date: Date): void {
    const formattedDate = this.formatDateToYYYYMMDD(date);
    this.store.dispatch(loadSlots({ date: formattedDate }));
  }

  private formatDateToYYYYMMDD(date: Date): string {
    const year = date.getFullYear();
    const month = this.padZero(date.getMonth() + 1);
    const dayOfMonth = this.padZero(date.getDate());
    return `${year}-${month}-${dayOfMonth}`;
  }

  private padZero(value: number): string {
    return value < 10 ? '0' + value : value.toString();
  }

  private formatDateToJalaliYYYYMMDD(date: Date): string {
    const { jy, jm, jd } = jalaali.toJalaali(date.getFullYear(), date.getMonth() + 1, date.getDate());
    return `${jy}-${this.padZero(jm)}-${this.padZero(jd)}`;
  }

  private ensureTimeHasSeconds(time: string | null | undefined): string {
    if (!time) {
      return '00:00:00';
    }

    if (time.length === 5) {
      return `${time}:00`;
    }

    return time;
  }

  onAddSlotToBasket(slot: TimelineSlotDetails | null): void {
    if (!slot) {
      return;
    }

    if (!this.userId) {
      return;
    }

    const reservationDate = this.formatDateToYYYYMMDD(this.selectedDate);
    const reservationDatePersian = this.formatDateToJalaliYYYYMMDD(this.selectedDate);
    const startTime = this.ensureTimeHasSeconds(slot.startTime ?? slot.start);
    const endTime = this.ensureTimeHasSeconds(slot.endTime ?? slot.end);
    const service = this.items.find(item => item.id === slot.serviceId);
    const price = slot.price ?? service?.price ?? 0;

    // --- CHANGE 1: Use composite key as slotId inside basket to keep items unique per service+slot
    const compositeSlotId = `${slot.serviceId}:${slot.slotId}`;

    const item: ReservationBasketItem = {
      slotId: compositeSlotId,           // <— composite id
      serviceId: slot.serviceId,
      providerId: slot.providerId,
      serviceName: slot.serviceName,
      reservationDate,
      reservationDatePersian,
      startTime,
      endTime,
      userId: this.userId,
      price,
      durationMinutes: slot.durationMinutes,
      status: ReservationStatus.IN_BASKET
    };

    this.store.dispatch(addSlotToBasket({ item }));
  }

  onRemoveFromBasket(slotId: string): void {
    if (!this.userId) {
      return;
    }

    // This now receives the composite slotId from the basket component
    this.store.dispatch(removeSlotFromBasket({ userId: this.userId, slotId }));
  }

  onCheckout(): void {
    const date = this.formatDateToYYYYMMDD(this.selectedDate);
    this.store.dispatch(checkoutBasket({ date }));
  }

  private mergeBasketSlots(
    slotsByService: Record<string, SlotDTO[]>,
    basket: ReservationBasketItem[]
  ): Record<string, SlotDTO[]> {
    if (!slotsByService) return {};

    const currentDate = this.formatDateToYYYYMMDD(this.selectedDate);
    const basketKeys = new Set(
      basket
        .filter(item => item.reservationDate === currentDate)
        .map(item => item.slotId.includes(':') ? item.slotId : `${item.serviceId}:${item.slotId}`)
    );

    const next: Record<string, SlotDTO[]> = {};

    Object.keys(slotsByService).forEach(serviceId => {
      next[serviceId] = (slotsByService[serviceId] ?? []).map(slot => {
        const key = `${serviceId}:${slot.slotId}`;
        if (!basketKeys.has(key)) {
          return slot;
        }

        return {
          ...slot,
          status: ReservationStatus.IN_BASKET,
          reservedBy: slot.reservedBy ?? this.userId ?? null
        };
      });
    });

    return next;
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any): void {
    this.isMobileView = event.target.innerWidth <= 768;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
