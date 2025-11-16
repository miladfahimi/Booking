import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { TimelineSlotDetails } from '../timeline-slot-modals.component';

@Component({
  selector: 'app-timeline-slot-modal-model-booked',
  templateUrl: './booked-slot-modal.model.component.html',
  styleUrls: ['./booked-slot-modal.model.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BookedSlotModalModelComponent {
  @Input() slot: TimelineSlotDetails | null = null;
  @Output() remind = new EventEmitter<void>();

  onRemind(): void {
    if (!this.slot) return;
    this.remind.emit();
  }
}
