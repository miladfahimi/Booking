// timeline-slot-modal.component.ts
import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';

type TimelineSlotStatus = 'available' | 'booked' | 'pending' | 'maintenance';

export interface TimelineSlotDetails {
  readonly slotId: string;
  readonly serviceName: string;
  readonly label: string;
  readonly start: string;
  readonly end: string;
  readonly durationMinutes: number;
  readonly status: TimelineSlotStatus;
  readonly statusLabel: string;
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
  @Output() addToBasket = new EventEmitter<void>();

  onClose(): void {
    this.close.emit();
  }

  onRemindIfCanceled() {
    // TODO: 
  }

  onAddToBasket(): void {
    this.addToBasket.emit();
  }
}
