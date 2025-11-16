import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { TimelineSlotDetails } from '../timeline-slot-modals.component';

@Component({
  selector: 'app-timeline-slot-modal-model-mine',
  templateUrl: './mine-slot-modal.model.component.html',
  styleUrls: ['./mine-slot-modal.model.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class MineSlotModalModelComponent {
  @Input() slot: TimelineSlotDetails | null = null;
  @Output() cancel = new EventEmitter<void>();

  get canCancel(): boolean {
    if (!this.slot?.startTime || this.slot.startTime.indexOf('T') === -1) return true;
    const start = new Date(this.slot.startTime);
    if (isNaN(start.getTime())) return true;
    const ms = start.getTime() - Date.now();
    const hours = ms / 36e5;
    return hours >= 24;
  }

  onCancel(): void {
    if (!this.slot) return;
    this.cancel.emit();
  }
}
