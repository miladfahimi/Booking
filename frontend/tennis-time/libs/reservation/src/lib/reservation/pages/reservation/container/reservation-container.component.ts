import { Component, HostListener, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { loadSlots } from '../../../store/reservation.actions';
import { selectSlots, selectSlotsLoading } from '../../../store/reservation.selectors';

@Component({
  selector: 'app-reservation-container',
  templateUrl: './reservation-container.component.html',
  styleUrls: ['./reservation-container.component.scss']
})
export class ReservationContainerComponent implements OnInit {
  isMobileView: boolean = window.innerWidth <= 768;
  timeSlots$: Observable<any[]>;
  loading$: Observable<boolean>;

  items: any[] = [
    { label: 'زمین 1', value: 30, selected: false },
    { label: 'زمین 2', value: 60, selected: true },
    { label: 'زمین 3', value: 120, selected: false },
    { label: 'زمین 4', value: 180, selected: false },
    { label: 'زمین 5', value: 1440, selected: false },
  ];

  constructor(private store: Store) {
    this.timeSlots$ = this.store.select(selectSlots).pipe(
      map(slots => {
        if (!slots) return [];

        const startTime = 9 * 60; // Assuming a start time of 9:00 AM
        const slotDuration = 30; // Example slot duration, in minutes
        return slots.map((status, index) => {
          const time = this.getTimeForIndex(index, startTime, slotDuration);
          return {
            time,
            status, // 'a' or 'b'
            selected: false
          };
        });
      })
    );

    this.loading$ = this.store.select(selectSlotsLoading);
  }

  ngOnInit(): void {
    this.store.dispatch(loadSlots({ serviceId: '3337096a-9f28-48f8-a355-03445606bd1b', date: '2024-08-26' }));
  }

  getTimeForIndex(index: number, startTime: number, slotDuration: number): string {
    const totalMinutes = startTime + index * slotDuration;
    const hours = Math.floor(totalMinutes / 60);
    const minutes = totalMinutes % 60;
    return `${this.padZero(hours)}:${this.padZero(minutes)}`;
  }

  // Manual implementation of padding without padStart
  padZero(value: number): string {
    return value < 10 ? '0' + value : value.toString();
  }

  onDaySelected(day: any) {
    const formattedDate = this.formatDateToYYYYMMDD(day.date);
    this.store.dispatch(loadSlots({ serviceId: '3337096a-9f28-48f8-a355-03445606bd1b', date: formattedDate }));
    console.log('%cSelected Day:', 'color: blue', day);
  }

  private formatDateToYYYYMMDD(date: Date): string {
    const year = date.getFullYear();
    const month = this.padZero(date.getMonth() + 1); // Months are 0-based in JS
    const dayOfMonth = this.padZero(date.getDate());

    return `${year}-${month}-${dayOfMonth}`;
  }

  selectDuration(duration: any) {
    console.log('%cDuration Selected:', 'color: purple', duration);
  }

  selectSlot(slot: any) {
    console.log('%cTime Slot Selected:', 'color: teal', slot);
  }

  book() {
    console.log('%cBooking reservation...', 'color: green');
  }

  cancel() {
    console.log('%cReservation cancelled.', 'color: red');
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.isMobileView = event.target.innerWidth <= 768;
  }
}
