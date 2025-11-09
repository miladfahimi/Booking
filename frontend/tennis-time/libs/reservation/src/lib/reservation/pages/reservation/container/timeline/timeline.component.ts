// timeline.component.ts
import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ServiceDTO, SlotDTO } from '../../../../types';

type TimelineStatus = 'available' | 'booked' | 'pending' | 'maintenance';

type SlotsByService = Record<string, SlotDTO[]>;

interface TimelineSlot {
  readonly id: string;
  readonly label: string;
  readonly start: string;
  readonly end: string;
  readonly durationMinutes: number;
  readonly status: TimelineStatus;
}

interface TimelineColumn {
  readonly id: string;
  readonly label: string;
  readonly slots: TimelineSlot[];
}

/** Matches the modal's expected shape */
interface TimelineSlotDetails {
  readonly slotId: string;
  readonly serviceName: string;
  readonly label: string;
  readonly start: string;
  readonly end: string;
  readonly durationMinutes: number;
  readonly status: TimelineStatus;
  readonly statusLabel: string;
}

@Component({
  selector: 'app-timeline',
  templateUrl: './timeline.component.html',
  styleUrls: ['./timeline.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TimelineComponent implements OnChanges {
  @Input() services: ServiceDTO[] = [];
  @Input() slotsByService: SlotsByService = {};

  readonly pixelsPerMinute = 2;
  readonly defaultStartHour = 8;
  readonly defaultEndHour = 21;
  readonly hourHeight = this.pixelsPerMinute * 60;

  private columnsSnapshot: TimelineColumn[] = [];

  /** Backing state for the modal input */
  private selectedSlotDetailsSnapshot: TimelineSlotDetails | null = null;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['services'] || changes['slotsByService']) {
      this.columnsSnapshot = this.buildColumns();
    }
  }

  get columns(): TimelineColumn[] {
    return this.columnsSnapshot;
  }

  /** Bind this in your template as shown in your snippet */
  get selectedSlotDetails(): TimelineSlotDetails | null {
    return this.selectedSlotDetailsSnapshot;
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

  private buildColumns(): TimelineColumn[] {
    if (!Array.isArray(this.services) || this.services.length === 0) {
      return [];
    }

    return this.services.map((service: ServiceDTO) => ({
      id: service.id,
      label: service.name,
      slots: this.buildSlotsForService(service)
    }));
  }

  private buildSlotsForService(service: ServiceDTO): TimelineSlot[] {
    const source = this.slotsByService?.[service.id] ?? service.slots ?? [];

    if (!Array.isArray(source) || source.length === 0) {
      return [];
    }

    return source
      .map(slot => this.mapToTimelineSlot(service, slot))
      .filter((slot): slot is TimelineSlot => slot !== null);
  }

  private mapToTimelineSlot(service: ServiceDTO, slot: SlotDTO | null | undefined): TimelineSlot | null {
    if (!slot) {
      return null;
    }

    const start = this.toHHmm(slot.time) ?? this.toHHmm(service.startTime);
    if (!start) {
      return null;
    }

    const end = this.toHHmm(slot.endTime);
    const durationMinutes = this.calculateDurationMinutes(start, end, service.slotDuration);

    if (durationMinutes <= 0) {
      return null;
    }

    const status = this.normalizeStatus(slot.status);

    return {
      id: slot.slotId || `${service.id}-${start}`,
      label: this.getStatusLabel(status),
      start,
      end: end || this.addMinutesToHHmm(start, durationMinutes) || start,
      durationMinutes,
      status
    };
  }

  private calculateDurationMinutes(start: string, end: string | null, fallbackDuration?: number | null): number {
    if (end) {
      const diff = this.diffMinutes(start, end);
      if (diff > 0) {
        return diff;
      }
    }

    if (typeof fallbackDuration === 'number' && fallbackDuration > 0) {
      return fallbackDuration;
    }

    return 0;
  }

  private normalizeStatus(status?: string | null): TimelineStatus {
    if (!status) {
      return 'maintenance';
    }

    const normalized = status.trim().toLowerCase();

    if (normalized.indexOf('book') !== -1) {
      return 'booked';
    }

    if (normalized.indexOf('pend') !== -1) {
      return 'pending';
    }

    if (['available', 'free', 'open'].indexOf(normalized) !== -1) {
      return 'available';
    }

    if (['reserved', 'unavailable', 'taken', 'closed', 'maintenance'].indexOf(normalized) !== -1) {
      return 'maintenance';
    }

    return 'maintenance';
  }

  private getStatusLabel(status: TimelineStatus): string {
    switch (status) {
      case 'booked':
        return 'رزرو شده';
      case 'pending':
        return 'در انتظار';
      case 'available':
        return 'در دسترس';
      default:
        return 'نامشخص';
    }
  }

  private toHHmm(value?: string | null): string | null {
    if (!value) {
      return null;
    }

    const parts = value.split(':');
    if (parts.length < 2) {
      return null;
    }

    const hours = Number(parts[0]);
    const minutes = Number(parts[1]);

    if (Number.isNaN(hours) || Number.isNaN(minutes)) {
      return null;
    }

    return `${this.pad(hours)}:${this.pad(minutes)}`;
  }

  private diffMinutes(startHHmm: string, endHHmm: string): number {
    const [sh, sm] = startHHmm.split(':').map(Number);
    const [eh, em] = endHHmm.split(':').map(Number);

    if ([sh, sm, eh, em].some(value => Number.isNaN(value))) {
      return 0;
    }

    return Math.max((eh * 60 + em) - (sh * 60 + sm), 0);
  }

  private allStartTimesHHmm(): string[] {
    const fromServices = (this.services ?? [])
      .map(service => this.toHHmm(service.startTime))
      .filter(this.isNonEmptyString);

    const fromSlots = (this.columnsSnapshot ?? []).reduce<string[]>((acc, column: TimelineColumn) => {
      const slots = column && column.slots ? column.slots : [];
      for (let i = 0; i < slots.length; i++) {
        acc.push(slots[i].start);
      }
      return acc;
    }, []);

    return fromServices.concat(fromSlots);
  }

  private allEndTimesHHmm(): string[] {
    const fromServices = (this.services ?? [])
      .map(service => this.toHHmm(service.endTime))
      .filter(this.isNonEmptyString);

    const fromSlots = (this.columnsSnapshot ?? []).reduce<string[]>((acc, column: TimelineColumn) => {
      const slots = column && column.slots ? column.slots : [];
      for (let i = 0; i < slots.length; i++) {
        const end = this.addMinutesToHHmm(slots[i].start, slots[i].durationMinutes);
        if (this.isNonEmptyString(end)) {
          acc.push(end);
        }
      }
      return acc;
    }, []);

    return fromServices.concat(fromSlots);
  }

  private addMinutesToHHmm(time: string, minutesToAdd: number): string | null {
    const [hours, minutes] = time.split(':').map(Number);

    if (Number.isNaN(hours) || Number.isNaN(minutes) || Number.isNaN(minutesToAdd)) {
      return null;
    }

    const totalMinutes = hours * 60 + minutes + minutesToAdd;
    if (!Number.isFinite(totalMinutes)) {
      return null;
    }

    const normalizedMinutes = Math.max(totalMinutes, 0);
    const resultHours = Math.floor(normalizedMinutes / 60);
    const resultMinutes = normalizedMinutes % 60;

    return `${this.pad(resultHours)}:${this.pad(resultMinutes)}`;
  }

  private calculateMinutesFromStart(time: string): number {
    const [hours, minutes] = time.split(':').map(Number);

    if (Number.isNaN(hours) || Number.isNaN(minutes)) {
      return 0;
    }

    return Math.max((hours - this.effectiveStartHour) * 60 + minutes, 0);
  }

  private pad(value: number): string {
    return value < 10 ? '0' + value.toString() : value.toString();
  }

  private get effectiveStartHour(): number {
    const times = this.allStartTimesHHmm();
    const min = times.reduce<number | null>((acc, t) => {
      const parts = t.split(':');
      const h = Number(parts[0]);
      if (Number.isNaN(h)) {
        return acc;
      }
      return acc === null ? h : Math.min(acc, h);
    }, null);
    return Math.min(this.defaultStartHour, min ?? this.defaultStartHour);
  }

  private get effectiveEndHour(): number {
    const times = this.allEndTimesHHmm();
    const max = times.reduce<number | null>((acc, t) => {
      const parts = t.split(':');
      const h = Number(parts[0]);
      if (Number.isNaN(h)) {
        return acc;
      }
      return acc === null ? h : Math.max(acc, h);
    }, null);
    return Math.max(this.defaultEndHour, max ?? this.defaultEndHour);
  }

  private isNonEmptyString(value: string | null): value is string {
    return typeof value === 'string' && value.length > 0;
  }

  /** Open modal with clicked slot's details */
  onSlotClick(slot: TimelineSlot, column: TimelineColumn): void {
    this.selectedSlotDetailsSnapshot = {
      slotId: slot.id,
      serviceName: column.label,
      label: slot.label,
      start: slot.start,
      end: slot.end,
      durationMinutes: slot.durationMinutes,
      status: slot.status,
      statusLabel: this.getStatusLabel(slot.status)
    };
  }

  /** Close modal */
  onModalClose(): void {
    this.selectedSlotDetailsSnapshot = null;
  }

  /** Handle add-to-basket action from modal */
  onAddToBasket(): void {
    if (!this.selectedSlotDetailsSnapshot) {
      return;
    }
    // Replace with your real handler
    console.log('Add to basket:', this.selectedSlotDetailsSnapshot);
  }
}
