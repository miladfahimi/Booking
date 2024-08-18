import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';

@Component({
  selector: 'app-reservation-calendar',
  templateUrl: './reservation-calendar.component.html',
  styleUrls: ['./reservation-calendar.component.scss']
})
export class ReservationCalendarComponent implements OnInit {
  @Input() isMobile: boolean = false;
  @Output() selectDay = new EventEmitter<any>();

  month: any[] = [];
  week: any[] = [];
  selectedDay: any = null;

  ngOnInit(): void {
    this.generateMonth();
    this.generateWeek();
  }

  generateMonth(baseDate = new Date()) {
    this.month = [];
    const currentMonth = baseDate.getMonth();
    const firstDay = new Date(baseDate.getFullYear(), currentMonth, 1);
    const lastDay = new Date(baseDate.getFullYear(), currentMonth + 1, 0);

    for (let day = firstDay; day <= lastDay; day.setDate(day.getDate() + 1)) {
      this.month.push({
        label: this.getDayLabel(day),
        date: new Date(day),
        selected: this.month.length === 0
      });
    }
    this.selectedDay = this.month[0];
  }

  generateWeek(baseDate = new Date()) {
    this.week = [];
    const firstDayOfWeek = new Date(baseDate.setDate(baseDate.getDate() - baseDate.getDay()));
    for (let i = 0; i < 7; i++) {
      const day = new Date(firstDayOfWeek);
      day.setDate(firstDayOfWeek.getDate() + i);
      this.week.push({
        label: this.getDayLabel(day),
        date: new Date(day),
        selected: i === 0
      });
    }
    this.selectedDay = this.week[0];
  }

  getDayLabel(date: Date) {
    const days = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];
    return days[date.getDay()];
  }

  onSelectDay(day: any) {
    console.log('%cDay Selected:', 'color: blue', day);
    if (this.isMobile) {
      this.week.forEach(d => d.selected = false);
    } else {
      this.month.forEach(d => d.selected = false);
    }
    day.selected = true;
    this.selectedDay = day;
    this.selectDay.emit(day);
  }

  onGoToPrevious() {
    console.log('%cGo to Previous:', 'color: orange');
  }

  onGoToNext() {
    console.log('%cGo to Next:', 'color: orange');
  }
}
