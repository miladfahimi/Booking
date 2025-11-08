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
  readonly startHour = 5;   // TODO: should be update by the first services that start to work
  readonly endHour = 21;

  readonly hourHeight = this.pixelsPerMinute * 60;
  readonly hourLabels = this.buildHourLabels();
  readonly timelineHeight = this.hourLabels.length * this.hourHeight;

  readonly demoColumns: TimelineColumn[] = [
    {
      id: 'court-1',
      label: '🥎',
      slots: [
        { id: 'c1-slot-1', label: 'Training', start: '08:30', durationMinutes: 60, status: 'booked' },
        { id: 'c1-slot-2', label: 'Open Play', start: '11:00', durationMinutes: 45, status: 'available' },
        { id: 'c1-slot-3', label: 'Coaching', start: '14:15', durationMinutes: 90, status: 'booked' },
        { id: 'c1-slot-4', label: 'Doubles', start: '18:00', durationMinutes: 60, status: 'booked' }
      ]
    },
    {
      id: 'court-2',
      label: '🥎',
      slots: [
        { id: 'c2-slot-1', label: 'Junior Camp', start: '09:00', durationMinutes: 120, status: 'maintenance' },
        { id: 'c2-slot-2', label: 'Available', start: '11:00', durationMinutes: 60, status: 'available' },
        { id: 'c2-slot-3', label: 'League Match', start: '16:00', durationMinutes: 90, status: 'booked' }
      ]
    },
    {
      id: 'court-3',
      label: '🥎',
      slots: [
        { id: 'c3-slot-1', label: 'Open Play', start: '08:00', durationMinutes: 30, status: 'available' },
        { id: 'c3-slot-2', label: 'Match Prep', start: '10:45', durationMinutes: 75, status: 'booked' },
        { id: 'c3-slot-3', label: 'Maintenance', start: '15:30', durationMinutes: 45, status: 'maintenance' },
        { id: 'c3-slot-4', label: 'Evening Match', start: '18:15', durationMinutes: 105, status: 'booked' }
      ]
    },
    {
      id: 'court-4',
      label: '🥎',
      slots: [
        { id: 'c4-slot-1', label: 'Lessons', start: '09:30', durationMinutes: 45, status: 'booked' },
        { id: 'c4-slot-2', label: 'Available', start: '10:15', durationMinutes: 60, status: 'available' },
        { id: 'c4-slot-3', label: 'Clinic', start: '17:00', durationMinutes: 60, status: 'booked' }
      ]
    },
    {
      id: 'court-5',
      label: '🥎',
      slots: [
        { id: 'c5-slot-1', label: 'Available', start: '08:15', durationMinutes: 45, status: 'available' },
        { id: 'c5-slot-2', label: 'Doubles', start: '09:30', durationMinutes: 90, status: 'booked' },
        { id: 'c5-slot-3', label: 'Coaching', start: '15:00', durationMinutes: 60, status: 'booked' },
        { id: 'c5-slot-4', label: 'Evening Open', start: '19:00', durationMinutes: 60, status: 'available' }
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
    return value < 10 ? '0' + value.toString() : value.toString();
  }
}