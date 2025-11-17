import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-timeline-slot-basket-mine',
  templateUrl: './timeline-slot-basket-mine.component.html',
  styleUrls: ['./timeline-slot-basket-mine.component.scss'],
})
export class TimelineSlotBasketMineComponent implements OnInit {
  @Input({ required: true }) label!: string;
  @Input({ required: true }) start!: string;
  @Input({ required: true }) end!: string;
  @Input({ required: true }) durationMinutes!: number;
  @Input({ required: true }) height!: number;


  ngOnInit() {

    console.log(this.height);
  }
}