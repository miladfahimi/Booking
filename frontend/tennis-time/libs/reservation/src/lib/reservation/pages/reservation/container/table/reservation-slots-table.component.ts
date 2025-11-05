import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { ServiceDTO, SlotDTO } from '../../../../types';

type SlotsByService = Record<string, SlotDTO[]>;

type NormalizedStatus = 'available' | 'booked' | 'pending' | 'unknown';

@Component({
  selector: 'app-reservation-slots-table',
  templateUrl: './reservation-slots-table.component.html',
  styleUrls: ['./reservation-slots-table.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ReservationSlotsTableComponent implements OnChanges {
  @Input() services: ServiceDTO[] = [];
  @Input() selectedServiceId: string | null = null;
  @Input() slotsByService: SlotsByService = {};

  @Output() selectService = new EventEmitter<ServiceDTO>();
  @Output() selectSlot = new EventEmitter<{ service: ServiceDTO; slot: SlotDTO }>();

  displayedHours: string[] = [];
  private selectedCellKey: string | null = null;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['services'] || changes['slotsByService']) {
      this.buildDisplayedHours();
    }

    if (changes['selectedServiceId'] && !changes['selectedServiceId'].firstChange) {
      const { previousValue, currentValue } = changes['selectedServiceId'];
      if (previousValue !== currentValue) {
        this.selectedCellKey = null;
      }
    }
  }

  onServiceHeaderClick(service: ServiceDTO): void {
    this.selectedCellKey = null;
    this.selectService.emit(service);
  }

  onSlotClick(service: ServiceDTO, slot: SlotDTO | null): void {
    if (!slot || this.isSlotDisabled(slot)) {
      return;
    }

    if (this.selectedServiceId !== service.id) {
      this.selectService.emit(service);
    }

    if (!slot.slotId) {
      return;
    }

    this.selectedCellKey = this.composeSelectionKey(service.id, slot.slotId);
    this.selectSlot.emit({ service, slot });
  }

  isSelected(serviceId: string, slotId?: string | null): boolean {
    if (!slotId) {
      return false;
    }

    return this.selectedCellKey === this.composeSelectionKey(serviceId, slotId);
  }

  getSlotFor(serviceId: string, hourLabel: string): SlotDTO | null {
    const slots = this.slotsByService?.[serviceId] ?? [];
    return slots.find(slot => this.extractHourLabel(slot.time) === hourLabel) ?? null;
  }

  getStatusClass(slot: SlotDTO | null): string {
    switch (this.normalizeStatus(slot?.status)) {
      case 'booked':
        return 'status-booked';
      case 'pending':
        return 'status-pending';
      case 'available':
        return 'status-available';
      default:
        return 'status-unknown';
    }
  }

  isSlotDisabled(slot: SlotDTO | null): boolean {
    return !slot || this.normalizeStatus(slot.status) === 'booked';
  }

  getCellAriaLabel(serviceName: string, slot: SlotDTO | null): string {
    if (!slot) {
      return `${serviceName} - No slot available`;
    }

    const hour = this.extractHourLabel(slot.time) ?? '';
    const statusLabel = this.getStatusLabel(slot);
    return `${serviceName} - ${statusLabel} at ${hour}`.trim();
  }

  getStatusLabel(slot: SlotDTO | null): string {
    switch (this.normalizeStatus(slot?.status)) {
      case 'booked':
        return 'Booked';
      case 'pending':
        return 'Pending';
      case 'available':
        return 'Available';
      default:
        return 'Unavailable';
    }
  }

  private buildDisplayedHours(): void {
    const hours = new Set<string>();

    this.services.forEach(service => {
      this.generateHoursFromRange(service.startTime, service.endTime).forEach(hour => hours.add(hour));

      const serviceSlots = this.slotsByService?.[service.id] ?? [];
      serviceSlots.forEach(slot => {
        const label = this.extractHourLabel(slot.time);
        if (label) {
          hours.add(label);
        }
      });
    });

    if (hours.size === 0) {
      for (let hour = 8; hour <= 20; hour++) {
        hours.add(`${this.leftPad(hour)}:00`);
      }
    }

    this.displayedHours = Array.from(hours).sort((a, b) => this.compareHourLabels(a, b));
  }

  private generateHoursFromRange(start?: string | null, end?: string | null): string[] {
    const startDate = this.createDateFromTime(start);
    const endDate = this.createDateFromTime(end);

    if (!startDate || !endDate) {
      return [];
    }

    const hours: string[] = [];
    const cursor = new Date(startDate.getTime());

    while (cursor <= endDate) {
      hours.push(this.formatHourLabel(cursor));
      cursor.setHours(cursor.getHours() + 1, 0, 0, 0);
    }

    return hours;
  }

  private createDateFromTime(time?: string | null): Date | null {
    if (!time) {
      return null;
    }

    const [hourStr, minuteStr = '0'] = time.split(':');
    const hour = Number.parseInt(hourStr ?? '', 10);
    const minute = Number.parseInt(minuteStr ?? '0', 10);

    if (Number.isNaN(hour) || Number.isNaN(minute)) {
      return null;
    }

    const date = new Date();
    date.setHours(hour, minute, 0, 0);
    return date;
  }

  private formatHourLabel(date: Date): string {
    const hour = this.leftPad(date.getHours());
    const minute = this.leftPad(date.getMinutes());
    return `${hour}:${minute}`;
  }

  private compareHourLabels(a: string, b: string): number {
    return this.hourLabelToMinutes(a) - this.hourLabelToMinutes(b);
  }

  private hourLabelToMinutes(label: string): number {
    const [hourStr, minuteStr = '0'] = label.split(':');
    const hour = Number.parseInt(hourStr ?? '0', 10);
    const minute = Number.parseInt(minuteStr ?? '0', 10);
    return hour * 60 + minute;
  }

  private extractHourLabel(time?: string | null): string | null {
    if (!time) {
      return null;
    }

    const [hourStr, minuteStr = '0'] = time.split(':');
    if (!hourStr) {
      return null;
    }

    const hour = this.leftPad(hourStr);
    const minute = this.leftPad(minuteStr);
    return `${hour}:${minute}`;
  }

  private normalizeStatus(status?: string | null): NormalizedStatus {
    if (!status) {
      return 'unknown';
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

    if (['booked', 'reserved', 'unavailable', 'taken'].indexOf(normalized) !== -1) {
      return 'booked';
    }

    return 'unknown';
  }

  private composeSelectionKey(serviceId: string, slotId: string): string {
    return `${serviceId}_${slotId}`;
  }

  private leftPad(value: string | number | undefined | null, length: number = 2): string {
    const base = value ?? '';
    let str = String(base);
    while (str.length < length) {
      str = '0' + str;
    }
    return str;
  }
}
