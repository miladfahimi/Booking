import { Component, HostListener, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { loadSlots } from '../../../store/reservation.actions';
import { selectSlots, selectSlotsLoading } from '../../../store/reservation.selectors';
import { SlotDTO } from '../../../types';

@Component({
  selector: 'app-reservation-container',
  templateUrl: './reservation-container.component.html',
  styleUrls: ['./reservation-container.component.scss']
})
export class ReservationContainerComponent implements OnInit {
  isMobileView: boolean = window.innerWidth <= 768;
  timeSlots$: Observable<SlotDTO[] | null>;
  loading$: Observable<boolean>;

  items: any[] = [
    { label: 'زمین 1', value: 30, selected: false },
    { label: 'زمین 2', value: 60, selected: true },
    { label: 'زمین 3', value: 120, selected: false },
    { label: 'زمین 4', value: 180, selected: false },
    { label: 'زمین 5', value: 1440, selected: false },
  ];

  constructor(private store: Store) {
    this.timeSlots$ = this.store.select(selectSlots);
    this.loading$ = this.store.select(selectSlotsLoading);
  }

  ngOnInit(): void {
    this.store.dispatch(loadSlots({ serviceId: '5c5a83a5-cc76-4b00-aeda-897a3896d453', date: '2024-08-26' }));
  }

  onDaySelected(day: any) {
    console.log('%cRaw Selected Day:', 'color: red', day.date); // Check the initial format
    const date = typeof day.date === 'string' ? this.parseDate(day.date) : new Date(day.date);
    console.log('%cParsed Date:', 'color: green', date); // Check after parsing
    const formattedDate = this.formatDateToYYYYMMDD(date);
    console.log('%cFormatted Date:', 'color: blue', formattedDate); // Final formatted date
    this.store.dispatch(loadSlots({ serviceId: '3337096a-9f28-48f8-a355-03445606bd1b', date: formattedDate }));
  }
  private parseDate(dateString: string): Date {
    const parts = dateString.split('/');
    // Assuming the format is dd/MM/yyyy
    const day = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10) - 1; // Months are 0-based in JS
    const year = parseInt(parts[2], 10);
    return new Date(year, month, day);
  }

  private formatDateToYYYYMMDD(date: Date): string {
    const year = date.getFullYear();
    const month = this.padZero(date.getMonth() + 1);
    const dayOfMonth = this.padZero(date.getDate());

    return `${year}-${month}-${dayOfMonth}`;
  }

  private padZero(value: number): string {
    return value < 10 ? '0' + value : value.toString();
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
