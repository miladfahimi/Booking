import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { TimelineSlotDetails } from '../timeline-slot-modals.component';

@Component({
  selector: 'app-timeline-slot-modal-model-available',
  templateUrl: './available-slot-modal.model.component.html',
  styleUrls: ['./available-slot-modal.model.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AvailableSlotModalModelComponent {
  @Input() slot: TimelineSlotDetails | null = null;
  @Output() add = new EventEmitter<void>();

  onAdd(): void {
    if (!this.slot) return;
    this.add.emit();
  }
}
