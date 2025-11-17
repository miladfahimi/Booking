import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-timeline-slot-booked',
  templateUrl: './timeline-slot-booked.component.html',
  styleUrls: ['./timeline-slot-booked.component.scss'],
})
export class TimelineSlotBookedComponent {
  @Input({ required: true }) label!: string;
  @Input({ required: true }) start!: string;
  @Input({ required: true }) end!: string;
  @Input({ required: true }) durationMinutes!: number;
}