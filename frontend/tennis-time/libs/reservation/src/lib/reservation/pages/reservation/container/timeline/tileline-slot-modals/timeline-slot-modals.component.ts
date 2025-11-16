// timeline-slot-modal.component.ts
import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { ReservationStatus } from 'libs/reservation/src/lib/reservation/types';



export interface TimelineSlotDetails {
  readonly slotId: string;
  readonly serviceId: string;
  readonly providerId: string;
  readonly serviceName: string;
  readonly label: string;
  readonly start: string;
  readonly end: string;
  readonly startTime: string | null;
  readonly endTime: string | null;
  readonly durationMinutes: number;
  readonly status: ReservationStatus;
  readonly statusLabel: string;
  readonly isMine: boolean;
  readonly price?: number | null;
}
@Component({
  selector: 'app-timeline-slot-modals',
  templateUrl: './timeline-slot-modals.component.html',
  styleUrls: ['./timeline-slot-modals.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TimelineSlotModalComponent {
  @Input() slot: TimelineSlotDetails | null = null;
  @Output() close = new EventEmitter<void>();
  @Output() addToBasket = new EventEmitter<TimelineSlotDetails>();
  @Output() cancel = new EventEmitter<string>();

  onClose(): void {
    this.close.emit();
  }

  get isMineSlot(): boolean {
    return !!this.slot && this.slot.status === ReservationStatus.CONFIRMED && !!this.slot.isMine;
  }

  get isBookedSlot(): boolean {
    return !!this.slot && this.slot.status === ReservationStatus.CONFIRMED && !this.slot.isMine;
  }

  get isAvailableSlot(): boolean {
    return (
      !!this.slot &&
      this.slot.status !== ReservationStatus.CONFIRMED &&
      this.slot.status !== ReservationStatus.PENDING
    );
  }

  get isPendingSlot(): boolean {
    return !!this.slot && this.slot.status === ReservationStatus.PENDING;
  }

  onCancel(): void {
    if (!this.slot) return;
    this.cancel.emit(this.slot.slotId);
  }

  onRemindIfCanceled() {
    // TODO:
  }


  onAddToBasket(): void {
    if (!this.slot) {
      return;
    }

    this.addToBasket.emit(this.slot);
  }
}