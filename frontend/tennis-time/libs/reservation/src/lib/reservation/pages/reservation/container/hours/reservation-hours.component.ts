import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-reservation-hours',
  templateUrl: './reservation-hours.component.html',
  styleUrls: ['./reservation-hours.component.scss']
})
export class ReservationHoursComponent {
  @Input() timeSlots: any[] = [];
  @Output() selectSlot = new EventEmitter<any>();

  onSelectSlot(slot: any) {
    this.timeSlots.forEach(s => s.selected = false);
    slot.selected = true;
    console.log('%cTime Slot Selected:', 'color: teal', slot);
    this.selectSlot.emit(slot);
  }
}
