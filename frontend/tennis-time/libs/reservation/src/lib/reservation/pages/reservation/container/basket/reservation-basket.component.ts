import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { ReservationBasketItem } from '../../../../types/reservation-basket.types';
import { PaymentInitiationResult } from '../../../../types/reservation-payment.types';

@Component({
  selector: 'app-reservation-basket',
  templateUrl: './reservation-basket.component.html',
  styleUrls: ['./reservation-basket.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ReservationBasketComponent {
  @Input() items: ReservationBasketItem[] = [];
  @Input() total = 0;
  @Input() loading = false;
  @Input() payment: PaymentInitiationResult | null = null;

  @Output() remove = new EventEmitter<string>();
  @Output() checkout = new EventEmitter<void>();

  onRemove(slotId: string): void {
    this.remove.emit(slotId);
  }

  onCheckout(): void {
    if (this.loading || !this.items.length) {
      return;
    }

    this.checkout.emit();
  }

  trackBySlotId(_: number, item: ReservationBasketItem): string {
    return item.slotId;
  }
}
