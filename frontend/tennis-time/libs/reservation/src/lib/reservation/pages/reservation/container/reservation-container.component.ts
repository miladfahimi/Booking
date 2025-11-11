import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, Subject } from 'rxjs';
import { map, takeUntil } from 'rxjs/operators';
import { loadProvidersWithServices, loadSlots } from '../../../store/reservation.actions';
import { selectProviders, selectSlots, selectSlotsByService, selectSlotsLoading } from '../../../store/reservation.selectors';
import { SlotDTO, ProviderDTO, ServiceDTO, CreateReservationPayload, ReservationStatus } from '../../../types';
import { ReservationCreationService } from '../../../services/reservation-creation.service';
import { CoreAuthService } from '@tennis-time/core';
import * as jalaali from 'jalaali-js';
import { TimelineSlotDetails } from './timeline/tileline-slot-modal/timeline-slot-modal.component';

@Component({
  selector: 'app-reservation-container',
  templateUrl: './reservation-container.component.html',
  styleUrls: ['./reservation-container.component.scss']
})
export class ReservationContainerComponent implements OnInit, OnDestroy {
  isMobileView: boolean = window.innerWidth <= 768;
  timeSlots$: Observable<Record<string, SlotDTO[]>>;
  loading$: Observable<boolean>;
  items: ServiceDTO[] = []; // ServiceDTO[] type
  selectedService: ServiceDTO | null = null; // Track the selected service
  selectedDate: Date = new Date(); // Track the selected date
  slotsByService: Record<string, SlotDTO[]> = {};

  private providerId = '11111111-1111-1111-1111-111111111111'; // Hardcoded provider ID
  private destroy$ = new Subject<void>();

  constructor(
    private store: Store,
    private reservationCreationService: ReservationCreationService,
    private coreAuthService: CoreAuthService
  ) {
    this.timeSlots$ = this.store.select(selectSlots);
    this.loading$ = this.store.select(selectSlotsLoading);
  }

  ngOnInit(): void {
    this.store.dispatch(loadProvidersWithServices()); // Load providers and services
    this.dispatchLoadSlotsForDate(this.selectedDate);

    this.store.select(selectProviders).pipe(
      takeUntil(this.destroy$),
      map((providers: ProviderDTO[] | null) => {
        if (!providers) return;

        // Find the provider by ID and generate items from its services
        const provider = providers.find(p => p.id === this.providerId);
        if (provider && provider.services) {
          // Ensure all services have selected initialized to false
          this.items = provider.services.map(service => ({
            ...service,
            selected: service.selected || false,
          }));
        }
      })
    ).subscribe();

    this.store.select(selectSlotsByService).pipe(
      takeUntil(this.destroy$)
    ).subscribe(slotsByService => {
      this.slotsByService = slotsByService ?? {};
    });
  }

  onDaySelected(day: any) {
    console.log('%cRaw Selected Day:', 'color: red', day.date);
    this.selectedDate = typeof day.date === 'string' ? this.parseDate(day.date) : new Date(day.date);
    console.log('%cParsed Date:', 'color: green', this.selectedDate);
    const formattedDate = this.formatDateToYYYYMMDD(this.selectedDate);
    console.log('%cFormatted Date:', 'color: blue', formattedDate);

    this.dispatchLoadSlotsForDate(this.selectedDate);
  }

  selectDuration(service: ServiceDTO) {
    // Reset selected property for all items
    this.items.forEach(d => d.selected = false);
    // Set selected to true for the clicked service
    service.selected = true;
    this.selectedService = service;
    console.log('%cService Selected:', 'color: purple', service);
  }

  onSlotPicked(event: { service: ServiceDTO; slot: SlotDTO }) {
    if (!event) {
      return;
    }

    this.selectDuration(event.service);
    this.selectSlot(event.slot);
  }

  private parseDate(dateString: string): Date {
    const parts = dateString.split('/');
    const day = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10) - 1;
    const year = parseInt(parts[2], 10);
    return new Date(year, month, day);
  }

  private dispatchLoadSlotsForDate(date: Date): void {
    const formattedDate = this.formatDateToYYYYMMDD(date);
    this.store.dispatch(loadSlots({ date: formattedDate }));
  }

  private formatDateToYYYYMMDD(date: Date): string {
    const year = date.getFullYear();
    const month = this.padZero(date.getMonth() + 1);
    const dayOfMonth = this.padZero(date.getDate());
    return `${year}-${month}-${dayOfMonth}`;
  }

  private padZero(value: number): string {
    return value < 10 ? '0' + value : value.toString();
  }

  private formatDateToJalaliYYYYMMDD(date: Date): string {
    const { jy, jm, jd } = jalaali.toJalaali(date.getFullYear(), date.getMonth() + 1, date.getDate());
    return `${jy}-${this.padZero(jm)}-${this.padZero(jd)}`;
  }

  private ensureTimeHasSeconds(time: string | null | undefined): string {
    if (!time) {
      return '00:00:00';
    }

    if (time.length === 5) {
      return `${time}:00`;
    }

    return time;
  }


  selectSlot(slot: SlotDTO) {
    console.log('%cTime Slot Selected:', 'color: teal', slot);
  }

  book() {
    console.log('%cBooking reservation...', 'color: green');
  }

  cancel() {
    console.log('%cReservation cancelled.', 'color: red');
  }

  onAddSlotToBasket(slot: TimelineSlotDetails | null): void {
    if (!slot) {
      return;
    }

    const userId = this.coreAuthService.getUserId();
    if (!userId) {
      console.error('Cannot create reservation: missing user id.');
      return;
    }

    const reservationDate = this.formatDateToYYYYMMDD(this.selectedDate);
    const reservationDatePersian = this.formatDateToJalaliYYYYMMDD(this.selectedDate);
    const startTime = this.ensureTimeHasSeconds(slot.startTime ?? slot.start);
    const endTime = this.ensureTimeHasSeconds(slot.endTime ?? slot.end);

    const payload: CreateReservationPayload = {
      reservationDate,
      reservationDatePersian,
      startTime,
      endTime,
      userId,
      providerId: slot.providerId,
      serviceId: slot.serviceId,
      status: slot.status as ReservationStatus
    };

    this.reservationCreationService.createReservation(payload)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: reservation => {
          console.log('%cReservation created successfully:', 'color: green', reservation);
          this.dispatchLoadSlotsForDate(this.selectedDate);
        },
        error: error => {
          console.error('Failed to create reservation.', error);
        }
      });
  }


  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.isMobileView = event.target.innerWidth <= 768;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
