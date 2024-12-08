import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ServiceDTO } from '../../../../types';

@Component({
  selector: 'app-reservation-items',
  templateUrl: './reservation-items.component.html',
  styleUrls: ['./reservation-items.component.scss']
})
export class ReservationItemsComponent {
  @Input() items: ServiceDTO[] = []; // List of services to display
  @Output() selectItem = new EventEmitter<ServiceDTO>(); // Emits the selected item

  onSelectDuration(item: ServiceDTO) {
    this.items.forEach(d => d.selected = false); // Deselect all items
    item.selected = true; // Select the clicked item
    console.log('%cService Selected:', 'color: purple', item);
    this.selectItem.emit(item);
  }
}
