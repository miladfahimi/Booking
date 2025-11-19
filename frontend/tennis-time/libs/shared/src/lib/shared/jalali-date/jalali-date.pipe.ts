import { Pipe, PipeTransform } from '@angular/core';
import * as jalaali from 'jalaali-js';

@Pipe({ name: 'jalaliDate', pure: true })
export class JalaliDatePipe implements PipeTransform {
  transform(value: unknown): string {
    const date = this.toDate(value);
    if (!date) return '';

    const { jy, jm, jd } = jalaali.toJalaali(
      date.getFullYear(),
      date.getMonth() + 1,
      date.getDate()
    );

    return `${this.padYear(jy)}/${this.padTwoDigits(jm)}/${this.padTwoDigits(jd)}`;
  }

  private toDate(value: unknown): Date | null {
    if (value === null || value === undefined) return null;
    if (value instanceof Date) return isNaN(value.getTime()) ? null : value;

    const parsed = new Date(String(value));
    return isNaN(parsed.getTime()) ? null : parsed;
  }

  private padYear(year: number): string {
    const s = year.toString();
    if (s.length >= 4) return s;
    return '0000'.slice(0, 4 - s.length) + s;
  }

  private padTwoDigits(value: number): string {
    const isNegative = value < 0;
    const s = Math.abs(value).toString();
    if (s.length >= 2) return isNegative ? '-' + s : s;
    return isNegative ? '-0' + s : '0' + s;
  }
}
