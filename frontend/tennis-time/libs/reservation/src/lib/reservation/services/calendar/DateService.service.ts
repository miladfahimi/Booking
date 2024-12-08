// DateService.ts
import { Injectable } from '@angular/core';
import * as jalaali from 'jalaali-js';
import { Day } from "../../types";

@Injectable({
  providedIn: 'root',
})
export class DateService {
  private isFarsi: boolean = true;  // Set this to true for Farsi, false for English

  private gregorianMonths = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];

  private jalaliMonths = [
    'Farvardin', 'Ordibehesht', 'Khordad', 'Tir', 'Mordad', 'Shahrivar',
    'Mehr', 'Aban', 'Azar', 'Dey', 'Bahman', 'Esfand'
  ];

  private gregorianMonthsFarsi = [
    'ژانویه', 'فوریه', 'مارس', 'آوریل', 'مه', 'ژوئن',
    'ژوئیه', 'اوت', 'سپتامبر', 'اکتبر', 'نوامبر', 'دسامبر'
  ];

  private jalaliMonthsFarsi = [
    'فروردین', 'اردیبهشت', 'خرداد', 'تیر', 'مرداد', 'شهریور',
    'مهر', 'آبان', 'آذر', 'دی', 'بهمن', 'اسفند'
  ];

  // Adjusted to start the week from Saturday
  private daysFarsi = ['ش', 'ی', 'د', 'س', 'چ', 'پ', 'ج']; // Saturday to Friday
  private days = ['Sa', 'Su', 'M', 'T', 'W', 'Th', 'F'];  // Saturday to Friday

  getGregorianMonthName(date: Date): string {
    const monthIndex = date.getMonth();
    const monthName = this.isFarsi ? this.gregorianMonthsFarsi[monthIndex] : this.gregorianMonths[monthIndex];
    const year = this.isFarsi ? this.convertNumberToFarsi(date.getFullYear().toString()) : date.getFullYear().toString();
    return `${monthName} ${year}`;
  }

  getJalaliMonthName(date: Date): string {
    const jDate = jalaali.toJalaali(date.getFullYear(), date.getMonth() + 1, date.getDate());
    const monthName = this.isFarsi ? this.jalaliMonthsFarsi[jDate.jm - 1] : this.jalaliMonths[jDate.jm - 1];
    const year = this.isFarsi ? this.convertNumberToFarsi(jDate.jy.toString()) : jDate.jy.toString();
    return `${monthName} ${year}`;
  }

  getDayLabels(): string[] {
    return this.isFarsi ? this.daysFarsi : this.days;
  }

  generateMonth(baseDate: Date = new Date()): Day[] {
    const month: Day[] = [];

    // Convert baseDate to Jalaali
    const jDate = jalaali.toJalaali(baseDate.getFullYear(), baseDate.getMonth() + 1, baseDate.getDate());
    const jalaliYear = jDate.jy;
    const jalaliMonth = jDate.jm;

    // Get Gregorian date for the first day of the Jalaali month
    const gregorianFirstDay = jalaali.toGregorian(jalaliYear, jalaliMonth, 1);
    const firstDayOfMonth = new Date(gregorianFirstDay.gy, gregorianFirstDay.gm - 1, gregorianFirstDay.gd);

    // Determine day of week for the first day (Saturday=0, Friday=6)
    const startDayOfWeek = (firstDayOfMonth.getDay() + 1) % 7; // Adjust so Saturday is 0

    // Number of days to display from previous month
    const daysFromPrevMonth = startDayOfWeek;

    // Get Gregorian date for the first day to display in the calendar
    const firstDayToDisplay = new Date(firstDayOfMonth);
    firstDayToDisplay.setDate(firstDayOfMonth.getDate() - daysFromPrevMonth);

    // Get total days in the Jalaali month
    const daysInJalaliMonth = jalaali.jalaaliMonthLength(jalaliYear, jalaliMonth);

    // Total days to display: 6 weeks * 7 days = 42
    for (let i = 0; i < 42; i++) {
      const currentDay = new Date(firstDayToDisplay);
      currentDay.setDate(firstDayToDisplay.getDate() + i);
      const jDay = jalaali.toJalaali(currentDay.getFullYear(), currentDay.getMonth() + 1, currentDay.getDate());

      const jalaliDate = this.isFarsi ? this.convertNumberToFarsi(`${jDay.jd}`) : `${jDay.jd}`;

      const isCurrentJalaliMonth = (jDay.jm === jalaliMonth) && (jDay.jy === jalaliYear);

      month.push({
        label: this.getDayLabel(currentDay),
        date: new Date(currentDay),
        jalaliDate: jalaliDate,
        selected: this.isSameDay(currentDay, baseDate),
        isToday: this.isSameDay(currentDay, new Date()),
        areOtherMonths: !isCurrentJalaliMonth
      });
    }

    return month;
  }

  generateWeek(baseDate: Date = new Date()): Day[] {
    const week: Day[] = [];
    const jDate = jalaali.toJalaali(baseDate.getFullYear(), baseDate.getMonth() + 1, baseDate.getDate());
    const jalaliYear = jDate.jy;
    const jalaliMonth = jDate.jm;

    // Get Gregorian date for the first day of the Jalaali week (assuming week starts on Saturday)
    const firstDayOfWeek = jalaali.toGregorian(jalaliYear, jalaliMonth, jDate.jd);
    const gregorianFirstDay = new Date(firstDayOfWeek.gy, firstDayOfWeek.gm - 1, firstDayOfWeek.gd);
    gregorianFirstDay.setDate(gregorianFirstDay.getDate() - ((gregorianFirstDay.getDay() + 1) % 7));

    for (let i = 0; i < 7; i++) {
      const currentDay = new Date(gregorianFirstDay);
      currentDay.setDate(gregorianFirstDay.getDate() + i);
      const jDay = jalaali.toJalaali(currentDay.getFullYear(), currentDay.getMonth() + 1, currentDay.getDate());

      const jalaliDate = this.isFarsi ? this.convertNumberToFarsi(`${jDay.jd}`) : `${jDay.jd}`;

      week.push({
        label: this.getDayLabel(currentDay),
        date: new Date(currentDay),
        jalaliDate: jalaliDate,
        selected: this.isSameDay(currentDay, baseDate),
        isToday: this.isSameDay(currentDay, new Date()),
        areOtherMonths: false // Assuming week is within the same Jalaali month
      });
    }

    return week;
  }

  private convertToJalali(date: Date): string {
    const jDate = jalaali.toJalaali(date.getFullYear(), date.getMonth() + 1, date.getDate());
    return this.isFarsi ? this.convertNumberToFarsi(`${jDate.jd}`) : `${jDate.jd}`;
  }

  private getDayLabel(date: Date): string {
    const dayIndex = (date.getDay() + 1) % 7; // Adjust so Saturday is 0
    return this.isFarsi ? this.daysFarsi[dayIndex] : this.days[dayIndex];
  }

  private convertNumberToFarsi(input: string): string {
    const englishToFarsiMap: { [key: string]: string } = {
      '0': '۰',
      '1': '۱',
      '2': '۲',
      '3': '۳',
      '4': '۴',
      '5': '۵',
      '6': '۶',
      '7': '۷',
      '8': '۸',
      '9': '۹'
    };

    return input.replace(/\d/g, (digit) => englishToFarsiMap[digit as keyof typeof englishToFarsiMap]);
  }

  private isSameDay(date1: Date, date2: Date): boolean {
    return date1.getFullYear() === date2.getFullYear() &&
      date1.getMonth() === date2.getMonth() &&
      date1.getDate() === date2.getDate();
  }
}
