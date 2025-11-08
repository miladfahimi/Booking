// timeline.component.ts
import { ChangeDetectionStrategy, Component } from '@angular/core';

type TimelineStatus = 'available' | 'booked' | 'maintenance';

interface TimelineSlot {
  readonly id: string;
  readonly label: string;
  readonly start: string; // HH:mm
  readonly durationMinutes: number;
  readonly status: TimelineStatus;
}

interface TimelineColumn {
  readonly id: string;
  readonly label: string;
  readonly slots: TimelineSlot[];
}

@Component({
  selector: 'app-timeline',
  templateUrl: './timeline.component.html',
  styleUrls: ['./timeline.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TimelineComponent {
  readonly pixelsPerMinute = 2;
  /** ساعت شروع پیش‌فرض اگر داده‌ها زودتر از این شروع نشوند */
  readonly defaultStartHour = 8;
  readonly endHour = 21;

  readonly hourHeight = this.pixelsPerMinute * 60;

  /** ---- داده‌های نمونه (فارسی) ---- */
  readonly demoColumns: TimelineColumn[] = [
    {
      id: 'court-1',
      label: ' زمین ۱',
      slots: [
        { id: 'c1-slot-1', label: 'تمرین', start: '08:30', durationMinutes: 60, status: 'booked' },
        { id: 'c1-slot-2', label: 'بازی آزاد', start: '11:00', durationMinutes: 45, status: 'available' },
        { id: 'c1-slot-3', label: 'مربی‌گری', start: '14:15', durationMinutes: 90, status: 'booked' },
        { id: 'c1-slot-4', label: 'دوبل', start: '18:00', durationMinutes: 60, status: 'booked' }
      ]
    },
    {
      id: 'court-2',
      label: ' زمین ۲',
      slots: [
        { id: 'c2-slot-1', label: 'اردوی نوجوانان', start: '09:00', durationMinutes: 120, status: 'maintenance' },
        { id: 'c2-slot-2', label: 'آزاد', start: '11:00', durationMinutes: 60, status: 'available' },
        { id: 'c2-slot-3', label: 'مسابقه لیگ', start: '16:00', durationMinutes: 90, status: 'booked' }
      ]
    },
    {
      id: 'court-3',
      label: ' زمین ۳',
      slots: [
        { id: 'c3-slot-1', label: 'بازی آزاد', start: '06:00', durationMinutes: 30, status: 'available' },
        { id: 'c3-slot-2', label: 'آمادگی مسابقه', start: '10:45', durationMinutes: 75, status: 'booked' },
        { id: 'c3-slot-3', label: 'تعمیرات', start: '15:30', durationMinutes: 45, status: 'maintenance' },
        { id: 'c3-slot-4', label: 'مسابقه عصر', start: '18:15', durationMinutes: 105, status: 'booked' }
      ]
    },
    {
      id: 'court-4',
      label: ' زمین ۴',
      slots: [
        { id: 'c4-slot-1', label: 'کلاس', start: '09:30', durationMinutes: 45, status: 'booked' },
        { id: 'c4-slot-2', label: 'آزاد', start: '10:15', durationMinutes: 60, status: 'available' },
        { id: 'c4-slot-3', label: 'کلینیک تمرینی', start: '17:00', durationMinutes: 60, status: 'booked' }
      ]
    },
    {
      id: 'court-5',
      label: ' زمین ۵',
      slots: [
        { id: 'c5-slot-1', label: 'آزاد', start: '08:15', durationMinutes: 45, status: 'available' },
        { id: 'c5-slot-2', label: 'دوبل', start: '09:30', durationMinutes: 90, status: 'booked' },
        { id: 'c5-slot-3', label: 'مربی‌گری', start: '15:00', durationMinutes: 60, status: 'booked' },
        { id: 'c5-slot-4', label: 'بازی آزاد عصر', start: '19:00', durationMinutes: 60, status: 'available' }
      ]
    }
  ];
  /** ---- محاسبات وابسته به داده‌ها ---- */
  readonly hourLabels = this.buildHourLabels();
  readonly timelineHeight = this.hourLabels.length * this.hourHeight;

  getSlotOffset(slot: TimelineSlot): number {
    return this.calculateMinutesFromStart(slot.start) * this.pixelsPerMinute;
  }

  getSlotHeight(slot: TimelineSlot): number {
    return Math.max(slot.durationMinutes * this.pixelsPerMinute, 1);
  }

  trackColumnById(_: number, column: TimelineColumn): string {
    return column.id;
  }

  trackSlotById(_: number, slot: TimelineSlot): string {
    return slot.id;
  }

  trackHourByValue(_: number, hour: string): string {
    return hour;
  }

  private buildHourLabels(): string[] {
    const labels: string[] = [];
    for (let hour = this.effectiveStartHour; hour <= this.endHour; hour++) {
      labels.push(`${this.pad(hour)}:00`);
    }
    return labels;
  }

  private calculateMinutesFromStart(time: string): number {
    const [hours, minutes] = time.split(':').map(Number);
    return Math.max((hours - this.effectiveStartHour) * 60 + minutes, 0);
  }

  private pad(value: number): string {
    return value < 10 ? '0' + value.toString() : value.toString();
  }

  /** تعیین پویا: اگر اسلاتی زودتر شروع شود، ساعت شروع همان لحاظ می‌شود */
  private get effectiveStartHour(): number {
    return Math.min(this.defaultStartHour, this.getEarliestSlotHour());
  }

  private getEarliestSlotHour(): number {
    let minHour = this.defaultStartHour;
    for (const col of this.demoColumns) {
      for (const s of col.slots) {
        const [h] = s.start.split(':').map(Number);
        if (!Number.isNaN(h) && h < minHour) {
          minHour = h;
        }
      }
    }
    return minHour;
  }
}
