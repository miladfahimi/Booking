import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-reservation-items',
  templateUrl: './reservation-items.component.html',
  styleUrls: ['./reservation-items.component.scss']
})
export class ReservationItemsComponent {
  @Input() items: any[] = [];
  @Output() selectItem = new EventEmitter<any>();

  onSelectDuration(item: any) {
    this.items.forEach(d => d.selected = false);
    item.selected = true;
    console.log('%cDuration Selected:', 'color: purple', item);
    this.selectItem.emit(item);
  }
}
