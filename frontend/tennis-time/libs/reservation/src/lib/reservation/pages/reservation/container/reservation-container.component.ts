import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, Subject } from 'rxjs';
import { map, takeUntil } from 'rxjs/operators';
import { loadProvidersWithServices, loadSlots } from '../../../store/reservation.actions';
import { selectProviders, selectService, selectSlots, selectSlotsLoading } from '../../../store/reservation.selectors';
import { SlotDTO, ProviderDTO, ServiceDTO } from '../../../types';

@Component({
  selector: 'app-reservation-container',
  templateUrl: './reservation-container.component.html',
  styleUrls: ['./reservation-container.component.scss']
})
export class ReservationContainerComponent implements OnInit, OnDestroy {
  isMobileView: boolean = window.innerWidth <= 768;
  timeSlots$: Observable<SlotDTO[] | null>;
  loading$: Observable<boolean>;
  items: ServiceDTO[] = []; // ServiceDTO[] type
  selectedService: ServiceDTO | null = null; // Track the selected service
  selectedDate: Date = new Date(); // Track the selected date
  slotsByService: Record<string, SlotDTO[]> = {};

  private providerId = '11111111-1111-1111-1111-111111111111'; // Hardcoded provider ID
  private destroy$ = new Subject<void>();

  constructor(private store: Store) {
    this.timeSlots$ = this.store.select(selectSlots);
    this.loading$ = this.store.select(selectSlotsLoading);
  }

  ngOnInit(): void {
    this.store.dispatch(loadProvidersWithServices()); // Load providers and services

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

          // Optionally, load slots for the first service by default
          if (this.items.length > 0) {
            this.selectDuration(this.items[0]);
          }
        }
      })
    ).subscribe();

    this.store.select(selectService).pipe(
      takeUntil(this.destroy$)
    ).subscribe(service => {
      if (!service) {
        return;
      }

      this.slotsByService = {
        ...this.slotsByService,
        [service.id]: service.slots ?? []
      };
    });
  }

  onDaySelected(day: any) {
    console.log('%cRaw Selected Day:', 'color: red', day.date);
    this.selectedDate = typeof day.date === 'string' ? this.parseDate(day.date) : new Date(day.date);
    console.log('%cParsed Date:', 'color: green', this.selectedDate);
    const formattedDate = this.formatDateToYYYYMMDD(this.selectedDate);
    console.log('%cFormatted Date:', 'color: blue', formattedDate);

    if (this.selectedService) {
      this.store.dispatch(loadSlots({ serviceId: this.selectedService.id, date: formattedDate }));
    }
  }

  selectDuration(service: ServiceDTO) {
    // Reset selected property for all items
    this.items.forEach(d => d.selected = false);
    // Set selected to true for the clicked service
    service.selected = true;
    this.selectedService = service;
    console.log('%cService Selected:', 'color: purple', service);

    // Load timeslots for the selected service based on the selected date
    const formattedDate = this.formatDateToYYYYMMDD(this.selectedDate);
    this.store.dispatch(loadSlots({ serviceId: service.id, date: formattedDate }));
  }

  onSelectService(service: ServiceDTO) {
    if (this.selectedService?.id === service.id) {
      this.items.forEach(d => d.selected = d.id === service.id);
      this.selectedService = service;
      return;
    }

    this.selectDuration(service);
  }

  onSlotPicked(event: { service: ServiceDTO; slot: SlotDTO }) {
    if (!event) {
      return;
    }

    if (!this.selectedService || this.selectedService.id !== event.service.id) {
      this.selectDuration(event.service);
    } else {
      this.items.forEach(d => d.selected = d.id === event.service.id);
    }

    this.selectedService = event.service;
    this.selectSlot(event.slot);
  }

  private parseDate(dateString: string): Date {
    const parts = dateString.split('/');
    const day = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10) - 1;
    const year = parseInt(parts[2], 10);
    return new Date(year, month, day);
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

  selectSlot(slot: SlotDTO) {
    console.log('%cTime Slot Selected:', 'color: teal', slot);
  }

  book() {
    console.log('%cBooking reservation...', 'color: green');
  }

  cancel() {
    console.log('%cReservation cancelled.', 'color: red');
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