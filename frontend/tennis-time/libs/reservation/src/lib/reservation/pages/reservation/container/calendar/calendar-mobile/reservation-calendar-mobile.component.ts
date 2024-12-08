import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { DateService } from '../../../../../services/calendar/DateService.service';
import { Day } from "../../../../../types";

@Component({
  selector: 'app-reservation-calendar-mobile',
  templateUrl: './reservation-calendar-mobile.component.html',
  styleUrls: ['./reservation-calendar-mobile.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ReservationCalendarMobileComponent implements OnInit {
  @Input() isMobile: boolean = false;
  @Output() selectDay = new EventEmitter<Day>();

  month: Day[] = [];
  week: Day[] = [];
  selectedDay: Day | null = null;
  gregorianMonthName: string = '';
  jalaliMonthName: string = '';
  baseDate: Date = new Date();

  constructor(private dateService: DateService) {}

  ngOnInit(): void {
    this.updateCalendarView();
  }

  private updateCalendarView(): void {
    this.gregorianMonthName = this.dateService.getGregorianMonthName(this.baseDate);
    this.jalaliMonthName = this.dateService.getJalaliMonthName(this.baseDate);

    if (this.isMobile) {
      this.week = this.dateService.generateWeek(this.baseDate);
    } else {
      this.month = this.dateService.generateMonth(this.baseDate);
    }

    this.clearSelection();
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
    this.selectedDay = null;
  }

  onGoToPrevious(): void {
    if (this.isMobile) {
      this.baseDate.setDate(this.baseDate.getDate() - 7);
    } else {
      this.baseDate.setMonth(this.baseDate.getMonth() - 1);
    }
    this.updateCalendarView();
  }

  onGoToNext(): void {
    if (this.isMobile) {
      this.baseDate.setDate(this.baseDate.getDate() + 7);
    } else {
      this.baseDate.setMonth(this.baseDate.getMonth() + 1);
    }
    this.updateCalendarView();
  }

  onGoToToday(): void {
    this.baseDate = new Date();
    this.updateCalendarView();
  }
}
