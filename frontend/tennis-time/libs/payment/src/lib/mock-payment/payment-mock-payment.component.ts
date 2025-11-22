import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable, Subject, Subscription, interval, timer } from 'rxjs';
import { filter, takeUntil } from 'rxjs/operators';
import { PaymentMockSessionData, PaymentSessionReservation } from '../types/payment.types';
import { MockPaymentSessionService } from '../services/mock/mock-payment-session.service';

@Component({
  selector: 'app-payment-mock-payment',
  templateUrl: './payment-mock-payment.component.html',
  styleUrls: ['./payment-mock-payment.component.scss']
})
export class PaymentMockPaymentComponent implements OnInit, OnDestroy {
  session: PaymentMockSessionData | null = null;
  reservations: PaymentSessionReservation[] = [];
  completionLoading$: Observable<boolean>;
  completionError$: Observable<string | null>;
  paymentCompleted = false;
  countdown = 30;
  private destroy$ = new Subject<void>();
  private tickSub?: Subscription;
  private autoCloseSub?: Subscription;

  constructor(
    private readonly store: Store,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly mockPaymentSession: MockPaymentSessionService
  ) {
    this.completionLoading$ = this.selectReservationState(state => state.paymentCompletionStatus?.loading ?? false);
    this.completionError$ = this.selectReservationState(state => state.paymentCompletionError ?? null);
  }

  ngOnInit(): void {
    const routePaymentId = this.route.snapshot.paramMap.get('paymentId');
    const session = this.mockPaymentSession.loadSession();

    if (!session || (routePaymentId && session.paymentId !== routePaymentId)) {
      this.mockPaymentSession.clear();
      this.router.navigateByUrl('/reservation/book');
      return;
    }

    this.session = session;
    this.reservations = session.reservations;

    this.selectReservationState(state => state.paymentCompletionStatus ?? { loading: false, loaded: false })
      .pipe(
        filter(status => status.loaded && !status.loading),
        takeUntil(this.destroy$)
      )
      .subscribe(() => {
        this.paymentCompleted = true;
        this.startCountdown();
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.tickSub?.unsubscribe();
    this.autoCloseSub?.unsubscribe();
  }

  onConfirmPayment(): void {
    if (!this.session) {
      return;
    }
    this.store.dispatch({ type: '[Reservation] Complete Mock Payment' });
  }

  private selectReservationState<T>(selector: (state: any) => T): Observable<T> {
    return this.store.select(state => selector((state as any).reservation ?? {}));
  }

  private startCountdown(): void {
    this.countdown = 30;
    this.tickSub?.unsubscribe();
    this.autoCloseSub?.unsubscribe();
    this.tickSub = interval(1000).pipe(takeUntil(this.destroy$)).subscribe(() => {
      if (this.countdown > 0) {
        this.countdown -= 1;
      }
    });
    this.autoCloseSub = timer(30000).pipe(takeUntil(this.destroy$)).subscribe(() => this.finishAndClose());
  }

  closeNow(e?: Event): void {
    if (e) e.preventDefault();
    this.finishAndClose();
  }

  private finishAndClose(): void {
    this.mockPaymentSession.clear();
    if (typeof window !== 'undefined') {
      window.close();
    }
  }
}