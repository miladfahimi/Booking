import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-timeline-slot-pending',
  templateUrl: './timeline-slot-pending.component.html',
  styleUrls: ['./timeline-slot-pending.component.scss'],
})
export class TimelineSlotPendingComponent {
  @Input({ required: true }) label!: string;
  @Input({ required: true }) start!: string;
  @Input({ required: true }) end!: string;
  @Input({ required: true }) durationMinutes!: number;
}