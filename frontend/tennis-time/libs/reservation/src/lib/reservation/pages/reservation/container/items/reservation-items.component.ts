import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ServiceDTO } from '../../../../types';

@Component({
  selector: 'app-reservation-items',
  templateUrl: './reservation-items.component.html',
  styleUrls: ['./reservation-items.component.scss']
})
export class ReservationItemsComponent {
  @Input() items: ServiceDTO[] = [];
  @Output() selectItem = new EventEmitter<ServiceDTO>();

  onSelectDuration(item: ServiceDTO) {
    this.items.forEach(d => d.selected = false);
    item.selected = true;
    console.log('%cService Selected:', 'color: purple', item);
    this.selectItem.emit(item);
  }
}
