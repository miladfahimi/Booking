import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { TimelineSlotDetails } from '../timeline-slot-modals.component';

@Component({
  selector: 'app-timeline-slot-modal-model-pending',
  templateUrl: './pending-slot-modal.model.component.html',
  styleUrls: ['./pending-slot-modal.model.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PendingSlotModalModelComponent {
  @Input() slot: TimelineSlotDetails | null = null;


  onRemind(): void {
    if (!this.slot) return;
    // Implement remind logic here
  }
}
