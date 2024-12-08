import { Component, Input, Output, EventEmitter } from '@angular/core';
import { SlotDTO } from '../../../../types';

@Component({
  selector: 'app-reservation-hours',
  templateUrl: './reservation-hours.component.html',
  styleUrls: ['./reservation-hours.component.scss']
})
export class ReservationHoursComponent {
  @Input() timeSlots: SlotDTO[] | null = [];
  @Output() selectSlot = new EventEmitter<SlotDTO>();

  selectedSlotId: string | null = null; // Track the selected slot ID

  onSelectSlot(slot: SlotDTO) {
    if (slot.status === 'booked') return; // Don't allow selection of booked slots

    this.selectedSlotId = slot.slotId; // Set the selected slot ID
    console.log('%cTime Slot Selected:', 'color: teal', slot);
    this.selectSlot.emit(slot);
  }

  isSelected(slot: SlotDTO): boolean {
    return this.selectedSlotId === slot.slotId;
  }
}
