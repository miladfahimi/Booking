import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-timeline-slot-basket',
  templateUrl: './timeline-slot-basket.component.html',
  styleUrls: ['./timeline-slot-basket.component.scss'],
})
export class TimelineSlotBasketComponent {
  @Input({ required: true }) label!: string;
  @Input({ required: true }) start!: string;
  @Input({ required: true }) end!: string;
  @Input({ required: true }) durationMinutes!: number;
  @Input({ required: true }) height!: number;
  @Input() showHoldIndicator = false;
}