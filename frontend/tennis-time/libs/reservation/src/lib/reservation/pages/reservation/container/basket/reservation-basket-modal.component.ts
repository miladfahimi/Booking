import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { ReservationBasketItem } from '../../../../types/reservation-basket.types';
import { PaymentInitiationResult } from '../../../../types/reservation-payment.types';

@Component({
  selector: 'app-reservation-basket-modal',
  templateUrl: './reservation-basket-modal.component.html',
  styleUrls: ['./reservation-basket-modal.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ReservationBasketModalComponent {
  @Input() items: ReservationBasketItem[] = [];
  @Input() total = 0;
  @Input() loading = false;
  @Input() payment: PaymentInitiationResult | null = null;

  @Output() remove = new EventEmitter<string>();
  @Output() checkout = new EventEmitter<void>();
  @Output() close = new EventEmitter<void>();

  onClose(): void {
    this.close.emit();
  }

  onRemove(slotId: string): void {
    this.remove.emit(slotId);
  }

  onCheckout(): void {
    this.checkout.emit();
  }
}
