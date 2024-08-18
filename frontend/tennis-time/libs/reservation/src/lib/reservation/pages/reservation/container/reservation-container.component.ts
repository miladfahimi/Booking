import {Component, HostListener} from '@angular/core';

@Component({
  selector: 'app-reservation-container',
  templateUrl: './reservation-container.component.html',
  styleUrls: ['./reservation-container.component.scss']
})
export class ReservationContainerComponent {
  isMobileView: boolean = window.innerWidth <= 768;

  items: any[] = [
    { label: 'زمین 1', value: 30, selected: false },
    { label: 'زمین 2', value: 60, selected: true },
    { label: 'زمین 3', value: 120, selected: false },
    { label: 'زمین 4', value: 180, selected: false },
    { label: 'زمین 5', value: 1440, selected: false },
  ];

  timeSlots: any[] = [
    { time: '09:00', selected: false },
    { time: '09:30', selected: false },
    { time: '10:00', selected: false },
    { time: '10:30', selected: false },
    { time: '11:00', selected: false },
    { time: '11:30', selected: false },
    { time: '12:00', selected: false },
    { time: '12:30', selected: false },
    { time: '13:00', selected: false },
    { time: '13:30', selected: false },
    { time: '14:00', selected: false },
    { time: '14:30', selected: false },
    { time: '15:00', selected: false },
    { time: '15:30', selected: false }
  ];

  book() {
    console.log('%cBooking reservation...', 'color: green');
  }

  cancel() {
    console.log('%cReservation cancelled.', 'color: red');
  }

  onDaySelected(day: any) {
    console.log('%cSelected Day:', 'color: blue', day);
  }

  selectDuration(duration: any) {
    console.log('%cDuration Selected:', 'color: purple', duration);
  }

  selectSlot(slot: any) {
    console.log('%cTime Slot Selected:', 'color: teal', slot);
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.isMobileView = event.target.innerWidth <= 768;
  }
}
