import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { DateService } from '../../../../services/calendar/DateService.service';
import {Day} from "../../../../types";

@Component({
  selector: 'app-reservation-calendar',
  templateUrl: './reservation-calendar.component.html',
  styleUrls: ['./reservation-calendar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ReservationCalendarComponent implements OnInit {
  @Input() isMobile: boolean = false;
  @Output() selectDay = new EventEmitter<Day>();

  month: Day[] = [];
  week: Day[] = [];
  selectedDay: Day | null = null;
  gregorianMonthName: string = '';
  jalaliMonthName: string = '';
  baseDate: Date = new Date();  // Track the current base date

  constructor(private dateService: DateService) {}

  ngOnInit(): void {
    this.updateCalendarView();
  }

  private updateCalendarView(): void {
    this.gregorianMonthName = this.dateService.getGregorianMonthName(this.baseDate);
    this.jalaliMonthName = this.dateService.getJalaliMonthName(this.baseDate);

    if (this.isMobile) {
      this.week = this.dateService.generateWeek(this.baseDate);
      this.selectedDay = this.week[0];
    } else {
      this.month = this.dateService.generateMonth(this.baseDate);
      this.selectedDay = this.month[0];
    }
  }

  onSelectDay(day: Day): void {
    this.clearSelection();
    day.selected = true;
    this.selectedDay = day;
    this.selectDay.emit(day);
  }

  private clearSelection(): void {
    if (this.isMobile) {
      this.week.forEach(d => d.selected = false);
    } else {
      this.month.forEach(d => d.selected = false);
    }
  }

  onGoToPrevious(): void {
    if (this.isMobile) {
      this.baseDate.setDate(this.baseDate.getDate() - 7);  // Go to the previous week
    } else {
      this.baseDate.setMonth(this.baseDate.getMonth() - 1);  // Go to the previous month
    }
    this.updateCalendarView();
  }

  onGoToNext(): void {
    if (this.isMobile) {
      this.baseDate.setDate(this.baseDate.getDate() + 7);  // Go to the next week
    } else {
      this.baseDate.setMonth(this.baseDate.getMonth() + 1);  // Go to the next month
    }
    this.updateCalendarView();
  }
}
