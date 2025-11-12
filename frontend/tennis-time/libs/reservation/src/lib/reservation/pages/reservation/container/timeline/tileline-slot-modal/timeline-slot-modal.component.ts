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
  selector: 'app-timeline-slot-modal',
  templateUrl: './timeline-slot-modal.component.html',
  styleUrls: ['./timeline-slot-modal.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TimelineSlotModalComponent {
  @Input() slot: TimelineSlotDetails | null = null;
  @Output() close = new EventEmitter<void>();
  @Output() addToBasket = new EventEmitter<TimelineSlotDetails>();
  @Output() cancel = new EventEmitter<string>();
  readonly ReservationStatusEnum = ReservationStatus;

  onClose(): void {
    this.close.emit();
  }

  onCancel(): void {
    if (!this.slot) return;
    this.cancel.emit(this.slot.slotId);
  }

  get canCancel(): boolean {
    if (!this.slot?.startTime || this.slot.startTime.indexOf('T') === -1) return true;
    const start = new Date(this.slot.startTime);
    if (isNaN(start.getTime())) return true;
    const ms = start.getTime() - Date.now();
    const hours = ms / 36e5;
    return hours >= 24;
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