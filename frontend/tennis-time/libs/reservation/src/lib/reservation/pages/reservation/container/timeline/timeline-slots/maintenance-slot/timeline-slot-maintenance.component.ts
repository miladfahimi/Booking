import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-timeline-slot-maintenance',
  templateUrl: './timeline-slot-maintenance.component.html',
  styleUrls: ['./timeline-slot-maintenance.component.scss'],
})
export class TimelineSlotMaintenanceComponent {
  @Input({ required: true }) label!: string;
  @Input({ required: true }) start!: string;
  @Input({ required: true }) end!: string;
  @Input({ required: true }) durationMinutes!: number;
  @Input({ required: true }) height!: number;
}