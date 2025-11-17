import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-timeline-slot-booked-mine',
  templateUrl: './timeline-slot-booked-mine.component.html',
  styleUrls: ['./timeline-slot-booked-mine.component.scss'],
})
export class TimelineSlotBookedMineComponent {
  @Input({ required: true }) label!: string;
  @Input({ required: true }) start!: string;
  @Input({ required: true }) end!: string;
  @Input({ required: true }) durationMinutes!: number;
}