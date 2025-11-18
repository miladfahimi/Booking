import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-timeline-slot-basket-mine',
  templateUrl: './timeline-slot-basket-mine.component.html',
  styleUrls: ['./timeline-slot-basket-mine.component.scss'],
})
export class TimelineSlotBasketMineComponent {
  @Input({ required: true }) label!: string;
  @Input({ required: true }) start!: string;
  @Input({ required: true }) end!: string;
  @Input({ required: true }) durationMinutes!: number;
  @Input({ required: true }) height!: number;
}