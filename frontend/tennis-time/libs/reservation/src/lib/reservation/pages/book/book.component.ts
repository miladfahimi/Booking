import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss']
})
export class BookComponent implements OnInit {
  week: any[] = [];
  selectedItem: string = '';
  durations: any[] = [
    { label: '30 زمین', value: 30, selected: false },
    { label: '1 زمین', value: 60, selected: true },
    { label: '2 زمین', value: 120, selected: false },
    { label: '3 زمین', value: 180, selected: false },
    { label: ' زمین', value: 1440, selected: false },
  ];
  // Updated timeSlots structure
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
  selectedDay: any = null;
  location: string = '';

  ngOnInit(): void {
    this.generateWeek();
  }

  generateWeek() {
    const today = new Date();
    for (let i = 0; i < 7; i++) {
      const day = new Date();
      day.setDate(today.getDate() + i);
      this.week.push({
        label: this.getDayLabel(day),
        date: day,
        selected: i === 0
      });
    }
    this.selectedDay = this.week[0];
  }

  getDayLabel(date: Date) {
    const days = ['M', 'T', 'W', 'T', 'F', 'S', 'S'];
    return days[date.getDay()];
  }

  selectDay(day: any) {
    this.week.forEach(d => d.selected = false);
    day.selected = true;
    this.selectedDay = day;
  }

  selectDuration(duration: any) {
    this.durations.forEach(d => d.selected = false);
    duration.selected = true;
  }

  selectSlot(slot: any) {
    this.timeSlots.forEach(s => s.selected = false);
    slot.selected = true;
  }
  book() {
    // Implement booking logic here
    console.log('Booking:', this.selectedItem, this.selectedDay, this.location);
  }

  cancel() {
    // Implement cancel logic here
    console.log('Cancelled');
  }
}
