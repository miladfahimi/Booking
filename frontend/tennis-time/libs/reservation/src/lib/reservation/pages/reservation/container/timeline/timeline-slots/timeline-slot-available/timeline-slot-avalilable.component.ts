import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-timeline-slot-available',
  templateUrl: './timeline-slot-available.component.html',
  styleUrls: ['./timeline-slot-available.component.scss'],
})
export class TimelineSlotAvailableComponent {
  @Input({ required: true }) label!: string;
  @Input({ required: true }) start!: string;
  @Input({ required: true }) end!: string;
  @Input({ required: true }) durationMinutes!: number;
  @Input({ required: true }) height!: number;
}