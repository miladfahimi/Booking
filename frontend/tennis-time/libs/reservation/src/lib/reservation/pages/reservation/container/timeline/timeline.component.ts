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
  readonly startHour = 8;
  readonly endHour = 21;

  readonly hourLabels = this.buildHourLabels();
  readonly hourHeight = this.pixelsPerMinute * 60;
  readonly timelineHeight = (this.endHour - this.startHour) * 60 * this.pixelsPerMinute;

  readonly demoColumns: TimelineColumn[] = [
    {
      id: 'court-1',
      label: 'Court 1',
      slots: [
        { id: 'slot-1', label: 'Training', start: '09:30', durationMinutes: 60, status: 'booked' },
        { id: 'slot-2', label: 'Available', start: '12:00', durationMinutes: 45, status: 'available' },
        { id: 'slot-3', label: 'Coaching', start: '17:15', durationMinutes: 90, status: 'booked' }
      ]
    },
    {
      id: 'court-2',
      label: 'Court 2',
      slots: [
        { id: 'slot-4', label: 'Maintenance', start: '10:00', durationMinutes: 30, status: 'maintenance' },
        { id: 'slot-5', label: 'Available', start: '13:30', durationMinutes: 60, status: 'available' },
        { id: 'slot-6', label: 'Match', start: '16:00', durationMinutes: 120, status: 'booked' }
      ]
    },
    {
      id: 'court-3',
      label: 'Court 3',
      slots: [
        { id: 'slot-4', label: 'Maintenance', start: '13:00', durationMinutes: 35, status: 'maintenance' },
        { id: 'slot-5', label: 'Available', start: '13:35', durationMinutes: 60, status: 'available' },
        { id: 'slot-6', label: 'Match', start: '16:00', durationMinutes: 120, status: 'booked' }
      ]
    },
    {
      id: 'court-4',
      label: 'Court 4',
      slots: [
        { id: 'slot-4', label: 'Maintenance', start: '11:00', durationMinutes: 55, status: 'maintenance' },
        { id: 'slot-5', label: 'Available', start: '13:45', durationMinutes: 20, status: 'available' },
        { id: 'slot-6', label: 'Match', start: '16:00', durationMinutes: 120, status: 'booked' }
      ]
    }
  ];

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

    for (let hour = this.startHour; hour <= this.endHour; hour++) {
      labels.push(`${this.pad(hour)}:00`);
    }

    return labels;
  }

  private calculateMinutesFromStart(time: string): number {
    const [hours, minutes] = time.split(':').map(Number);
    return Math.max((hours - this.startHour) * 60 + minutes, 0);
  }

  private pad(value: number): string {
    // Avoid using String.prototype.padStart to remain compatible with older TS lib targets.
    return value < 10 ? '0' + value : value.toString();
  }
}
