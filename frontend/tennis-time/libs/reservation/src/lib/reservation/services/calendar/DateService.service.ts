import { Injectable } from '@angular/core';
import * as jalaali from 'jalaali-js';

interface Day {
  label: string;
  date: Date;
  jalaliDate: string;
  selected: boolean;
}

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

  private days = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];
  private daysFarsi = ['ش', 'ی', 'د', 'س', 'چ', 'پ', 'ج'];

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
  generateMonth(baseDate: Date = new Date()): Day[] {
    const month: Day[] = [];
    const currentMonth = baseDate.getMonth();
    const firstDay = new Date(baseDate.getFullYear(), currentMonth, 1);
    const lastDay = new Date(baseDate.getFullYear(), currentMonth + 1, 0);

    for (let day = firstDay; day <= lastDay; day.setDate(day.getDate() + 1)) {
      const jalaliDate = this.convertToJalali(day);
      const dateLabel = this.isFarsi ? this.convertNumberToFarsi(day.getDate().toString()) : day.getDate().toString();
      month.push({
        label: this.getDayLabel(day),
        date: new Date(day),
        jalaliDate,
        selected: month.length === 0,
      });
    }

    return month;
  }

  generateWeek(baseDate: Date = new Date()): Day[] {
    const week: Day[] = [];
    const firstDayOfWeek = new Date(baseDate.setDate(baseDate.getDate() - baseDate.getDay()));
    for (let i = 0; i < 7; i++) {
      const day = new Date(firstDayOfWeek);
      day.setDate(firstDayOfWeek.getDate() + i);
      const jalaliDate = this.convertToJalali(day);
      week.push({
        label: this.getDayLabel(day),
        date: new Date(day),
        jalaliDate,
        selected: i === 0,
      });
    }

    return week;
  }

  private convertToJalali(date: Date): string {
    const jDate = jalaali.toJalaali(date.getFullYear(), date.getMonth() + 1, date.getDate());
    return this.isFarsi ? this.convertNumberToFarsi(`${jDate.jd}`) : `${jDate.jd}`;
  }


  private getDayLabel(date: Date): string {
    const dayIndex = date.getDay();
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
}
