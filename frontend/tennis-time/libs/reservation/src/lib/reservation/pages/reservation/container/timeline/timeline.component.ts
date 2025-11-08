// timeline.component.ts
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

type TimelineStatus = 'available' | 'booked' | 'maintenance';

interface BackendSlot {
  readonly slotId: string;
  readonly time: string;
  readonly endTime: string;
  readonly status: TimelineStatus;
  readonly price: number;
  readonly capacity: number;
}

interface BackendCourt {
  readonly id: string;
  readonly name: string;
  readonly type: string;
  readonly availability: boolean;
  readonly providerId: string;
  readonly startTime: string;
  readonly endTime: string;
  readonly slotDuration: number;
  readonly slotGapDuration: number;
  readonly tags: string[];
  readonly price: number;
  readonly maxCapacity: number;
  readonly slots: BackendSlot[];
  readonly slotCount: number;
}

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
export class TimelineComponent implements OnInit {
  readonly pixelsPerMinute = 2;
  readonly defaultStartHour = 8;
  readonly defaultEndHour = 21;
  readonly hourHeight = this.pixelsPerMinute * 60;

  courts: BackendCourt[] = [];

  ngOnInit(): void {
    this.setData(this.mockCourts);
  }

  setData(courts: BackendCourt[]): void {
    this.courts = Array.isArray(courts) ? courts : [];
  }

  get columns(): TimelineColumn[] {
    return this.courts.map((c: BackendCourt) => ({
      id: c.id,
      label: c.name,
      slots: (c.slots || []).map((s: BackendSlot) => ({
        id: s.slotId,
        label: s.status === 'available' ? '' : '',
        start: this.toHHmm(s.time),
        durationMinutes: this.diffMinutes(this.toHHmm(s.time), this.toHHmm(s.endTime)),
        status: s.status
      }))
    }));
  }

  get hourLabels(): string[] {
    const labels: string[] = [];
    for (let hour = this.effectiveStartHour; hour <= this.effectiveEndHour; hour++) {
      labels.push(`${this.pad(hour)}:00`);
    }
    return labels;
  }

  get timelineHeight(): number {
    return this.hourLabels.length * this.hourHeight;
  }

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

  private toHHmm(value: string): string {
    const parts = value.split(':').map(Number);
    if (parts.length >= 2) {
      const [h, m] = parts;
      return `${this.pad(h)}:${this.pad(m)}`;
    }
    return value;
  }

  private diffMinutes(startHHmm: string, endHHmm: string): number {
    const [sh, sm] = startHHmm.split(':').map(Number);
    const [eh, em] = endHHmm.split(':').map(Number);
    return Math.max((eh * 60 + em) - (sh * 60 + sm), 0);
  }

  private allStartTimesHHmm(): string[] {
    const fromCourts = this.courts.map((c: BackendCourt) => this.toHHmm(c.startTime));
    const fromSlots = this.courts.reduce<string[]>((acc: string[], c: BackendCourt) => {
      const items = (c.slots || []).map((s: BackendSlot) => this.toHHmm(s.time));
      acc.push(...items);
      return acc;
    }, []);
    return [...fromCourts, ...fromSlots].filter(Boolean);
  }

  private allEndTimesHHmm(): string[] {
    const fromCourts = this.courts.map((c: BackendCourt) => this.toHHmm(c.endTime));
    const fromSlots = this.courts.reduce<string[]>((acc: string[], c: BackendCourt) => {
      const items = (c.slots || []).map((s: BackendSlot) => this.toHHmm(s.endTime));
      acc.push(...items);
      return acc;
    }, []);
    return [...fromCourts, ...fromSlots].filter(Boolean);
  }

  private calculateMinutesFromStart(time: string): number {
    const [hours, minutes] = time.split(':').map(Number);
    return Math.max((hours - this.effectiveStartHour) * 60 + minutes, 0);
  }

  private pad(value: number): string {
    return value < 10 ? '0' + value.toString() : value.toString();
  }

  private get effectiveStartHour(): number {
    const times = this.allStartTimesHHmm();
    const min = times.reduce<number | null>((acc, t) => {
      const [h] = t.split(':').map(Number);
      if (Number.isNaN(h)) return acc;
      return acc === null ? h : Math.min(acc, h);
    }, null);
    return Math.min(this.defaultStartHour, min ?? this.defaultStartHour);
  }

  private get effectiveEndHour(): number {
    const times = this.allEndTimesHHmm();
    const max = times.reduce<number | null>((acc, t) => {
      const [h] = t.split(':').map(Number);
      if (Number.isNaN(h)) return acc;
      return acc === null ? h : Math.max(acc, h);
    }, null);
    return Math.max(this.defaultEndHour, max ?? this.defaultEndHour);
  }

  private readonly mockCourts: BackendCourt[] = [
    {
      "id": "fb30f4a4-ac1f-4d8c-8025-cde28b059855",
      "name": "زمین ۱",
      "type": "خاک رس",
      "availability": true,
      "providerId": "11111111-1111-1111-1111-111111111111",
      "startTime": "08:00:00",
      "endTime": "18:00:00",
      "slotDuration": 30,
      "slotGapDuration": 0,
      "tags": [
        "[\"مبتدی\"",
        " \"بیرون\"]"
      ],
      "price": 100,
      "maxCapacity": 10,
      "slots": [
        {
          "slotId": "slot-1",
          "time": "08:00",
          "endTime": "08:30",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-2",
          "time": "08:30",
          "endTime": "08:55",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-3",
          "time": "09:00",
          "endTime": "09:25",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-4",
          "time": "09:30",
          "endTime": "09:55",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-5",
          "time": "10:00",
          "endTime": "10:25",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-6",
          "time": "10:30",
          "endTime": "10:55",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-7",
          "time": "11:00",
          "endTime": "11:25",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-8",
          "time": "11:30",
          "endTime": "11:55",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-9",
          "time": "12:00",
          "endTime": "12:25",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-10",
          "time": "12:30",
          "endTime": "12:55",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-11",
          "time": "13:00",
          "endTime": "13:25",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-12",
          "time": "13:30",
          "endTime": "13:55",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-13",
          "time": "14:00",
          "endTime": "14:25",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-14",
          "time": "14:30",
          "endTime": "14:55",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-15",
          "time": "15:00",
          "endTime": "15:25",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-16",
          "time": "15:30",
          "endTime": "15:55",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-17",
          "time": "16:00",
          "endTime": "16:25",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-18",
          "time": "16:30",
          "endTime": "16:55",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-19",
          "time": "17:00",
          "endTime": "17:25",
          "status": "available",
          "price": 100,
          "capacity": 10
        },
        {
          "slotId": "slot-20",
          "time": "17:30",
          "endTime": "17:55",
          "status": "available",
          "price": 100,
          "capacity": 10
        }
      ],
      "slotCount": 20
    },
    {
      "id": "b965e40c-1943-453a-9f53-aea91e0ee203",
      "name": "زمین ۲",
      "type": "چمن مصنوعی",
      "availability": true,
      "providerId": "11111111-1111-1111-1111-111111111111",
      "startTime": "09:00:00",
      "endTime": "20:00:00",
      "slotDuration": 45,
      "slotGapDuration": 10,
      "tags": [
        "[\"پیشرفته\"",
        " \"بیرون\"]"
      ],
      "price": 120,
      "maxCapacity": 15,
      "slots": [
        {
          "slotId": "slot-1",
          "time": "09:00",
          "endTime": "09:35",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-2",
          "time": "09:45",
          "endTime": "10:20",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-3",
          "time": "10:30",
          "endTime": "11:05",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-4",
          "time": "11:15",
          "endTime": "11:50",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-5",
          "time": "12:00",
          "endTime": "12:35",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-6",
          "time": "12:45",
          "endTime": "13:20",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-7",
          "time": "13:30",
          "endTime": "14:05",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-8",
          "time": "14:15",
          "endTime": "14:50",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-9",
          "time": "15:00",
          "endTime": "15:35",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-10",
          "time": "15:45",
          "endTime": "16:20",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-11",
          "time": "16:30",
          "endTime": "17:05",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-12",
          "time": "17:15",
          "endTime": "17:50",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-13",
          "time": "18:00",
          "endTime": "18:35",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-14",
          "time": "18:45",
          "endTime": "19:20",
          "status": "available",
          "price": 120,
          "capacity": 15
        },
        {
          "slotId": "slot-15",
          "time": "19:30",
          "endTime": "20:05",
          "status": "available",
          "price": 120,
          "capacity": 15
        }
      ],
      "slotCount": 15
    },
    {
      "id": "b8861c4e-8fa3-4568-a0ab-c3092f2209f6",
      "name": "زمین ۳",
      "type": "خاک رس",
      "availability": true,
      "providerId": "11111111-1111-1111-1111-111111111111",
      "startTime": "07:00:00",
      "endTime": "19:00:00",
      "slotDuration": 30,
      "slotGapDuration": 5,
      "tags": [
        "[\"مبتدی\"",
        " \"بیرون\"]"
      ],
      "price": 110,
      "maxCapacity": 12,
      "slots": [
        {
          "slotId": "slot-1",
          "time": "07:00",
          "endTime": "07:25",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-2",
          "time": "07:30",
          "endTime": "07:55",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-3",
          "time": "08:00",
          "endTime": "08:25",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-4",
          "time": "08:30",
          "endTime": "08:55",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-5",
          "time": "09:00",
          "endTime": "09:25",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-6",
          "time": "09:30",
          "endTime": "09:55",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-7",
          "time": "10:00",
          "endTime": "10:25",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-8",
          "time": "10:30",
          "endTime": "10:55",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-9",
          "time": "11:00",
          "endTime": "11:25",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-10",
          "time": "11:30",
          "endTime": "11:55",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-11",
          "time": "12:00",
          "endTime": "12:25",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-12",
          "time": "12:30",
          "endTime": "12:55",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-13",
          "time": "13:00",
          "endTime": "13:25",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-14",
          "time": "13:30",
          "endTime": "13:55",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-15",
          "time": "14:00",
          "endTime": "14:25",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-16",
          "time": "14:30",
          "endTime": "14:55",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-17",
          "time": "15:00",
          "endTime": "15:25",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-18",
          "time": "15:30",
          "endTime": "15:55",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-19",
          "time": "16:00",
          "endTime": "16:25",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-20",
          "time": "16:30",
          "endTime": "16:55",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-21",
          "time": "17:00",
          "endTime": "17:25",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-22",
          "time": "17:30",
          "endTime": "17:55",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-23",
          "time": "18:00",
          "endTime": "18:25",
          "status": "available",
          "price": 110,
          "capacity": 12
        },
        {
          "slotId": "slot-24",
          "time": "18:30",
          "endTime": "18:55",
          "status": "available",
          "price": 110,
          "capacity": 12
        }
      ],
      "slotCount": 24
    },
    {
      "id": "04cbb8f5-28dd-4ada-86f2-3f1e8338933d",
      "name": "زمین ۴",
      "type": "چمن مصنوعی",
      "availability": true,
      "providerId": "11111111-1111-1111-1111-111111111111",
      "startTime": "10:00:00",
      "endTime": "22:00:00",
      "slotDuration": 30,
      "slotGapDuration": 5,
      "tags": [
        "[\"پیشرفته\"",
        " \"بیرون\"]"
      ],
      "price": 130,
      "maxCapacity": 20,
      "slots": [
        {
          "slotId": "slot-1",
          "time": "10:00",
          "endTime": "10:25",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-2",
          "time": "10:30",
          "endTime": "10:55",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-3",
          "time": "11:00",
          "endTime": "11:25",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-4",
          "time": "11:30",
          "endTime": "11:55",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-5",
          "time": "12:00",
          "endTime": "12:25",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-6",
          "time": "12:30",
          "endTime": "12:55",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-7",
          "time": "13:00",
          "endTime": "13:25",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-8",
          "time": "13:30",
          "endTime": "13:55",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-9",
          "time": "14:00",
          "endTime": "14:25",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-10",
          "time": "14:30",
          "endTime": "14:55",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-11",
          "time": "15:00",
          "endTime": "15:25",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-12",
          "time": "15:30",
          "endTime": "15:55",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-13",
          "time": "16:00",
          "endTime": "16:25",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-14",
          "time": "16:30",
          "endTime": "16:55",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-15",
          "time": "17:00",
          "endTime": "17:25",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-16",
          "time": "17:30",
          "endTime": "17:55",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-17",
          "time": "18:00",
          "endTime": "18:25",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-18",
          "time": "18:30",
          "endTime": "18:55",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-19",
          "time": "19:00",
          "endTime": "19:25",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-20",
          "time": "19:30",
          "endTime": "19:55",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-21",
          "time": "20:00",
          "endTime": "20:25",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-22",
          "time": "20:30",
          "endTime": "20:55",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-23",
          "time": "21:00",
          "endTime": "21:25",
          "status": "available",
          "price": 130,
          "capacity": 20
        },
        {
          "slotId": "slot-24",
          "time": "21:30",
          "endTime": "21:55",
          "status": "available",
          "price": 130,
          "capacity": 20
        }
      ],
      "slotCount": 24
    },
    {
      "id": "24de97dd-8980-47ac-958f-73f6df80b395",
      "name": "زمین ۵",
      "type": "خاک رس",
      "availability": true,
      "providerId": "11111111-1111-1111-1111-111111111111",
      "startTime": "06:00:00",
      "endTime": "16:00:00",
      "slotDuration": 25,
      "slotGapDuration": 5,
      "tags": [
        "[\"مبتدی\"",
        " \"بیرون\"]"
      ],
      "price": 95,
      "maxCapacity": 8,
      "slots": [
        {
          "slotId": "slot-1",
          "time": "06:00",
          "endTime": "06:20",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-2",
          "time": "06:25",
          "endTime": "06:45",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-3",
          "time": "06:50",
          "endTime": "07:10",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-4",
          "time": "07:15",
          "endTime": "07:35",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-5",
          "time": "07:40",
          "endTime": "08:00",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-6",
          "time": "08:05",
          "endTime": "08:25",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-7",
          "time": "08:30",
          "endTime": "08:50",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-8",
          "time": "08:55",
          "endTime": "09:15",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-9",
          "time": "09:20",
          "endTime": "09:40",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-10",
          "time": "09:45",
          "endTime": "10:05",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-11",
          "time": "10:10",
          "endTime": "10:30",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-12",
          "time": "10:35",
          "endTime": "10:55",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-13",
          "time": "11:00",
          "endTime": "11:20",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-14",
          "time": "11:25",
          "endTime": "11:45",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-15",
          "time": "11:50",
          "endTime": "12:10",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-16",
          "time": "12:15",
          "endTime": "12:35",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-17",
          "time": "12:40",
          "endTime": "13:00",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-18",
          "time": "13:05",
          "endTime": "13:25",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-19",
          "time": "13:30",
          "endTime": "13:50",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-20",
          "time": "13:55",
          "endTime": "14:15",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-21",
          "time": "14:20",
          "endTime": "14:40",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-22",
          "time": "14:45",
          "endTime": "15:05",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-23",
          "time": "15:10",
          "endTime": "15:30",
          "status": "available",
          "price": 95,
          "capacity": 8
        },
        {
          "slotId": "slot-24",
          "time": "15:35",
          "endTime": "15:55",
          "status": "available",
          "price": 95,
          "capacity": 8
        }
      ],
      "slotCount": 24
    },
    {
      "id": "a71fa1c1-4b9a-42e2-966b-0c837444e7b6",
      "name": "زمین ۶",
      "type": "چمن مصنوعی",
      "availability": true,
      "providerId": "11111111-1111-1111-1111-111111111111",
      "startTime": "11:00:00",
      "endTime": "23:00:00",
      "slotDuration": 60,
      "slotGapDuration": 15,
      "tags": [
        "[\"پیشرفته\"",
        " \"بیرون\"]"
      ],
      "price": 140,
      "maxCapacity": 25,
      "slots": [
        {
          "slotId": "slot-1",
          "time": "11:00",
          "endTime": "11:45",
          "status": "available",
          "price": 140,
          "capacity": 25
        },
        {
          "slotId": "slot-2",
          "time": "12:00",
          "endTime": "12:45",
          "status": "available",
          "price": 140,
          "capacity": 25
        },
        {
          "slotId": "slot-3",
          "time": "13:00",
          "endTime": "13:45",
          "status": "available",
          "price": 140,
          "capacity": 25
        },
        {
          "slotId": "slot-4",
          "time": "14:00",
          "endTime": "14:45",
          "status": "available",
          "price": 140,
          "capacity": 25
        },
        {
          "slotId": "slot-5",
          "time": "15:00",
          "endTime": "15:45",
          "status": "available",
          "price": 140,
          "capacity": 25
        },
        {
          "slotId": "slot-6",
          "time": "16:00",
          "endTime": "16:45",
          "status": "available",
          "price": 140,
          "capacity": 25
        },
        {
          "slotId": "slot-7",
          "time": "17:00",
          "endTime": "17:45",
          "status": "available",
          "price": 140,
          "capacity": 25
        },
        {
          "slotId": "slot-8",
          "time": "18:00",
          "endTime": "18:45",
          "status": "available",
          "price": 140,
          "capacity": 25
        },
        {
          "slotId": "slot-9",
          "time": "19:00",
          "endTime": "19:45",
          "status": "available",
          "price": 140,
          "capacity": 25
        },
        {
          "slotId": "slot-10",
          "time": "20:00",
          "endTime": "20:45",
          "status": "available",
          "price": 140,
          "capacity": 25
        },
        {
          "slotId": "slot-11",
          "time": "21:00",
          "endTime": "21:45",
          "status": "available",
          "price": 140,
          "capacity": 25
        },
        {
          "slotId": "slot-12",
          "time": "22:00",
          "endTime": "22:45",
          "status": "available",
          "price": 140,
          "capacity": 25
        }
      ],
      "slotCount": 12
    },
    {
      "id": "e03b5809-5ca1-450b-a197-74f6f8353af5",
      "name": "زمین ۷",
      "type": "خاک رس",
      "availability": true,
      "providerId": "e58c61d3-6aef-4a0d-9e7d-eef587bbb02e",
      "startTime": "08:30:00",
      "endTime": "18:30:00",
      "slotDuration": 35,
      "slotGapDuration": 0,
      "tags": [
        "[\"مبتدی\"",
        " \"بیرون\"]"
      ],
      "price": 105,
      "maxCapacity": 14,
      "slots": [
        {
          "slotId": "slot-1",
          "time": "08:30",
          "endTime": "09:02",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-2",
          "time": "09:05",
          "endTime": "09:37",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-3",
          "time": "09:40",
          "endTime": "10:12",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-4",
          "time": "10:15",
          "endTime": "10:47",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-5",
          "time": "10:50",
          "endTime": "11:22",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-6",
          "time": "11:25",
          "endTime": "11:57",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-7",
          "time": "12:00",
          "endTime": "12:32",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-8",
          "time": "12:35",
          "endTime": "13:07",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-9",
          "time": "13:10",
          "endTime": "13:42",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-10",
          "time": "13:45",
          "endTime": "14:17",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-11",
          "time": "14:20",
          "endTime": "14:52",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-12",
          "time": "14:55",
          "endTime": "15:27",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-13",
          "time": "15:30",
          "endTime": "16:02",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-14",
          "time": "16:05",
          "endTime": "16:37",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-15",
          "time": "16:40",
          "endTime": "17:12",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-16",
          "time": "17:15",
          "endTime": "17:47",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-17",
          "time": "17:50",
          "endTime": "18:22",
          "status": "available",
          "price": 105,
          "capacity": 14
        },
        {
          "slotId": "slot-18",
          "time": "18:25",
          "endTime": "18:57",
          "status": "available",
          "price": 105,
          "capacity": 14
        }
      ],
      "slotCount": 18
    }
  ];
}
