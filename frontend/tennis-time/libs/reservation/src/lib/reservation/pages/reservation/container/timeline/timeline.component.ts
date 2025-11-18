import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges
} from '@angular/core';
import { ReservationStatus, ServiceDTO, SlotDTO } from '../../../../types';
import { TimelineSlotDetails } from './tileline-slot-modals/timeline-slot-modals.component';
import { AuthFacadeService } from 'libs/shared/src/lib/shared/auth/auth-facade-service';

type SlotsByService = Record<string, SlotDTO[]>;

interface TimelineSlot {
  readonly id: string;
  readonly label: string;
  readonly start: string;
  readonly end: string;
  readonly durationMinutes: number;
  readonly status: ReservationStatus;
  readonly original: SlotDTO;
}

interface TimelineColumn {
  readonly id: string;
  readonly label: string;
  readonly slots: TimelineSlot[];
  readonly service: ServiceDTO;
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
  @Output() addToBasket = new EventEmitter<TimelineSlotDetails>();

  readonly pixelsPerMinute = 2;
  readonly defaultStartHour = 8;
  readonly defaultEndHour = 21;
  readonly hourHeight = this.pixelsPerMinute * 60;
  readonly ReservationStatusEnum = ReservationStatus;

  private columnsSnapshot: TimelineColumn[] = [];
  private currentUserId: string | null = null;
  private selectedSlotDetailsSnapshot: TimelineSlotDetails | null = null;

  /**
   * فقط اسلات‌هایی که روی‌شان کلیک شده را لاگ می‌کنیم.
   */
  private debugSlotIds = new Set<string>();

  /**
   * آخرین snapshot از وضعیت نمایشی هر اسلات برای مقایسه و لاگ فقط در صورت تغییر.
   */
  private debugSnapshots = new Map<
    string,
    {
      status: ReservationStatus;
      label: string;
      inBasketByCurrentUser?: boolean | null;
      inBasketByOtherUsers?: boolean | null;
      totalBasketUsers?: number | null;
      isMyBasketSlot: boolean;
      hasAnyBasketHold: boolean;
      hasForeignBasketHold: boolean;
      isMyBookedSlot: boolean;
    }
  >();

  constructor(private auth: AuthFacadeService) {
    this.auth.userId$.subscribe(id => {
      this.currentUserId = id;
      // اگر خواستی، می‌تونی این لاگ را هم موقتاً فعال کنی:
      // console.log('[Timeline][Auth] currentUserId updated', { currentUserId: this.currentUserId });
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['services'] || changes['slotsByService']) {
      this.columnsSnapshot = this.buildColumns();
    }
  }

  get columns(): TimelineColumn[] {
    return this.columnsSnapshot;
  }

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

  // ---------------------------
  // Helper برای لاگ *فقط وقتی چیزی عوض شده*
  // ---------------------------

  private logSlotIfChanged(
    context: string,
    slot: TimelineSlot,
    snapshot: {
      status: ReservationStatus;
      label: string;
      inBasketByCurrentUser?: boolean | null;
      inBasketByOtherUsers?: boolean | null;
      totalBasketUsers?: number | null;
      isMyBasketSlot: boolean;
      hasAnyBasketHold: boolean;
      hasForeignBasketHold: boolean;
      isMyBookedSlot: boolean;
    }
  ): void {
    if (!this.debugSlotIds.has(slot.id)) {
      return;
    }

    const prev = this.debugSnapshots.get(slot.id);
    const changed =
      !prev ||
      prev.status !== snapshot.status ||
      prev.label !== snapshot.label ||
      prev.inBasketByCurrentUser !== snapshot.inBasketByCurrentUser ||
      prev.inBasketByOtherUsers !== snapshot.inBasketByOtherUsers ||
      prev.totalBasketUsers !== snapshot.totalBasketUsers ||
      prev.isMyBasketSlot !== snapshot.isMyBasketSlot ||
      prev.hasAnyBasketHold !== snapshot.hasAnyBasketHold ||
      prev.hasForeignBasketHold !== snapshot.hasForeignBasketHold ||
      prev.isMyBookedSlot !== snapshot.isMyBookedSlot;

    if (!changed) {
      return;
    }

    this.debugSnapshots.set(slot.id, snapshot);

    console.log('[Timeline][' + context + ']', {
      slotId: slot.id,
      originalSlotId: slot.original?.slotId,
      start: slot.start,
      end: slot.end,
      durationMinutes: slot.durationMinutes,
      status: snapshot.status,
      label: snapshot.label,
      basketState: slot.original?.basketState,
      inBasketByCurrentUser: snapshot.inBasketByCurrentUser,
      inBasketByOtherUsers: snapshot.inBasketByOtherUsers,
      totalBasketUsers: snapshot.totalBasketUsers,
      isMyBasketSlot: snapshot.isMyBasketSlot,
      hasAnyBasketHold: snapshot.hasAnyBasketHold,
      hasForeignBasketHold: snapshot.hasForeignBasketHold,
      isMyBookedSlot: snapshot.isMyBookedSlot
    });
  }

  // ---------------------------
  // Building columns & slots
  // ---------------------------

  private buildColumns(): TimelineColumn[] {
    if (!Array.isArray(this.services) || this.services.length === 0) {
      return [];
    }

    return this.services.map((service: ServiceDTO) => ({
      id: service.id,
      label: service.name,
      slots: this.buildSlotsForService(service),
      service
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

    const status = this.resolveSlotStatus(slot);

    const timelineSlot: TimelineSlot = {
      id: slot.slotId || `${service.id}-${start}`,
      label: this.getStatusLabel(status),
      start,
      end: end || this.addMinutesToHHmm(start, durationMinutes) || start,
      durationMinutes,
      status,
      original: slot
    };

    return timelineSlot;
  }

  // ---------------------------
  // Status resolution
  // ---------------------------

  private resolveSlotStatus(slot: SlotDTO): ReservationStatus {
    const normalizedStatus = this.normalizeStatus(slot.status);
    const basketState = slot.basketState;

    let finalStatus: ReservationStatus;

    if (normalizedStatus === ReservationStatus.CONFIRMED) {
      finalStatus = ReservationStatus.CONFIRMED;
    } else if (normalizedStatus === ReservationStatus.PENDING) {
      finalStatus = ReservationStatus.PENDING;
    } else if (normalizedStatus === ReservationStatus.MAINTENANCE) {
      finalStatus = ReservationStatus.MAINTENANCE;
    } else if (normalizedStatus === ReservationStatus.IN_BASKET) {
      finalStatus = ReservationStatus.IN_BASKET;
    } else if ((basketState?.totalBasketUsers ?? 0) > 0) {
      finalStatus = ReservationStatus.IN_BASKET;
    } else {
      finalStatus = normalizedStatus;
    }

    return finalStatus;
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

  private normalizeStatus(status?: ReservationStatus | null): ReservationStatus {
    if (!status) {
      return ReservationStatus.MAINTENANCE;
    }

    const normalized = String(status).trim().toUpperCase();

    switch (normalized) {
      case ReservationStatus.AVAILABLE:
        return ReservationStatus.AVAILABLE;
      case ReservationStatus.PENDING:
        return ReservationStatus.PENDING;
      case ReservationStatus.IN_BASKET:
        return ReservationStatus.IN_BASKET;
      case ReservationStatus.CONFIRMED:
      case ReservationStatus.ADMIN_HOLD:
        return ReservationStatus.CONFIRMED;
      case ReservationStatus.MAINTENANCE:
        return ReservationStatus.MAINTENANCE;
      case ReservationStatus.CANCELED:
      case ReservationStatus.EXPIRED:
        return ReservationStatus.AVAILABLE;
      default:
        break;
    }

    if (normalized.indexOf('BOOK') !== -1) {
      return ReservationStatus.CONFIRMED;
    }

    if (normalized.indexOf('BASKET') !== -1) {
      return ReservationStatus.IN_BASKET;
    }

    if (normalized.indexOf('PEND') !== -1) {
      return ReservationStatus.PENDING;
    }

    if (['AVAILABLE', 'FREE', 'OPEN'].indexOf(normalized) !== -1) {
      return ReservationStatus.AVAILABLE;
    }

    if (['RESERVED', 'UNAVAILABLE', 'TAKEN', 'CLOSED', 'MAINTENANCE'].indexOf(normalized) !== -1) {
      return ReservationStatus.MAINTENANCE;
    }

    return ReservationStatus.MAINTENANCE;
  }

  private getStatusLabel(status: ReservationStatus): string {
    switch (status) {
      case ReservationStatus.CONFIRMED:
        return 'رزرو شده';
      case ReservationStatus.PENDING:
        return 'در انتظار';
      case ReservationStatus.IN_BASKET:
        return 'در سبد پرداخت';
      case ReservationStatus.AVAILABLE:
        return 'در دسترس';
      default:
        return 'نامشخص';
    }
  }

  // ---------------------------
  // Time helpers
  // ---------------------------

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

    return Math.max(eh * 60 + em - (sh * 60 + sm), 0);
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

  // ---------------------------
  // Basket / ownership helpers
  // ---------------------------

  getSlotLabel(slot: TimelineSlot): string {
    const isMineBooked = this.isMyBookedSlot(slot);
    const isMineBasket = this.isMyBasketSlot(slot);
    const anyHold = this.hasAnyBasketHold(slot);
    const foreignHold = this.hasForeignBasketHold(slot);

    let label: string;
    if (isMineBooked) {
      label = 'رزرو شده توسط شما';
    } else if (isMineBasket) {
      label = 'در سبد پرداخت شما';
    } else if (anyHold) {
      label = this.getStatusLabel(ReservationStatus.IN_BASKET);
    } else {
      label = slot.label;
    }

    this.logSlotIfChanged('getSlotLabel', slot, {
      status: slot.status,
      label,
      inBasketByCurrentUser: slot.original.basketState?.inBasketByCurrentUser,
      inBasketByOtherUsers: slot.original.basketState?.inBasketByOtherUsers,
      totalBasketUsers: slot.original.basketState?.totalBasketUsers,
      isMyBasketSlot: isMineBasket,
      hasAnyBasketHold: anyHold,
      hasForeignBasketHold: foreignHold,
      isMyBookedSlot: isMineBooked
    });

    return label;
  }

  isMyBasketSlot(slot: TimelineSlot): boolean {
    return (
      slot.status === ReservationStatus.IN_BASKET &&
      Boolean(slot.original.basketState?.inBasketByCurrentUser)
    );
  }

  hasAnyBasketHold(slot: TimelineSlot): boolean {
    return (
      slot.status === ReservationStatus.IN_BASKET &&
      (slot.original.basketState?.totalBasketUsers ?? 0) > 0
    );
  }

  hasForeignBasketHold(slot: TimelineSlot): boolean {
    return (
      slot.status === ReservationStatus.IN_BASKET &&
      Boolean(slot.original.basketState?.inBasketByOtherUsers)
    );
  }

  isMyBookedSlot(slot: TimelineSlot): boolean {
    return (
      slot.status === ReservationStatus.CONFIRMED &&
      !!this.currentUserId &&
      slot.original.reservedBy === this.currentUserId
    );
  }

  // ---------------------------
  // UI events
  // ---------------------------

  onSlotClick(slot: TimelineSlot, column: TimelineColumn): void {
    // از این لحظه این اسلات تحت نظر است
    this.debugSlotIds.add(slot.id);

    const isMine = this.isMyBookedSlot(slot);
    const isMineBasket = this.isMyBasketSlot(slot);
    const anyHold = this.hasAnyBasketHold(slot);
    const foreignHold = this.hasForeignBasketHold(slot);

    console.log('[Timeline][onSlotClick]', {
      slotId: slot.id,
      originalSlotId: slot.original?.slotId,
      columnId: column.id,
      columnLabel: column.label,
      status: slot.status,
      start: slot.start,
      end: slot.end,
      basketState: slot.original?.basketState,
      isMyBookedSlot: isMine,
      isMyBasketSlot: isMineBasket,
      hasAnyBasketHold: anyHold,
      hasForeignBasketHold: foreignHold
    });

    this.selectedSlotDetailsSnapshot = {
      slotId: slot.original.slotId || slot.id,
      serviceId: column.service.id,
      providerId: column.service.providerId,
      serviceName: column.label,
      label: isMine ? 'رزرو شده توسط شما' : this.getSlotLabel(slot),
      start: slot.start,
      end: slot.end,
      startTime: slot.original.time ?? slot.start,
      endTime: slot.original.endTime ?? slot.end,
      durationMinutes: slot.durationMinutes,
      status: slot.status,
      statusLabel: isMine ? 'رزرو شده توسط شما' : this.getStatusLabel(slot.status),
      isMine,
      price: slot.original.price
    };
  }

  onModalClose(): void {
    this.selectedSlotDetailsSnapshot = null;
  }

  onAddToBasket(details: TimelineSlotDetails): void {
    console.log('[Timeline][onAddToBasket] emitting addToBasket', details);
    this.addToBasket.emit(details);
    this.onModalClose();
  }
}
