import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'faDigits', pure: true })
export class FaDigitsPipe implements PipeTransform {
  private readonly map = ['۰','۱','۲','۳','۴','۵','۶','۷','۸','۹'];

  transform(value: unknown): string {
    if (value === null || value === undefined) return '';
    return String(value).replace(/\d/g, d => this.map[+d]);
  }
}
