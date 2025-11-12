import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable, Subject, interval, Subscription, timer } from 'rxjs';
import { filter, takeUntil } from 'rxjs/operators';
import { ReservationSummary } from '../../types/reservation-basket.types';
import { MockPaymentSessionService, MockPaymentSessionData } from '../../services/mock/mock-payment-session.service';
import { completeMockPayment } from '../../store/reservation.actions';
import { selectPaymentCompletionError, selectPaymentCompletionLoading, selectPaymentCompletionStatus } from '../../store/reservation.selectors';

@Component({
  selector: 'app-reservation-mock-payment',
  templateUrl: './reservation-mock-payment.component.html',
  styleUrls: ['./reservation-mock-payment.component.scss']
})
export class ReservationMockPaymentComponent implements OnInit, OnDestroy {
  session: MockPaymentSessionData | null = null;
  reservations: ReservationSummary[] = [];
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
    this.completionLoading$ = this.store.select(selectPaymentCompletionLoading);
    this.completionError$ = this.store.select(selectPaymentCompletionError);
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

    this.store.select(selectPaymentCompletionStatus)
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
    this.store.dispatch(completeMockPayment());
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
