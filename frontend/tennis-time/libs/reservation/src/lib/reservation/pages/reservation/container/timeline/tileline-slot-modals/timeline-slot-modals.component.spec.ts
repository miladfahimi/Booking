import { ChangeDetectionStrategy } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimelineSlotModalComponent, TimelineSlotDetails } from './timeline-slot-modals.component';
import { MineSlotModalModelComponent } from './mine-slot-modal/mine-slot-modal.model.component';
import { AvailableSlotModalModelComponent } from './available-slot-modal/available-slot-modal.model.component';
import { BookedSlotModalModelComponent } from './booked-slot-modal/booked-slot-modal.model.component';
import { ReservationStatus } from 'libs/reservation/src/lib/reservation/types';
import { PendingSlotModalModelComponent } from './pending-slot-modal/pending-slot-modal.model.component';

describe('TimelineSlotModalComponent', () => {
  let component: TimelineSlotModalComponent;
  let fixture: ComponentFixture<TimelineSlotModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        TimelineSlotModalComponent,
        MineSlotModalModelComponent,
        AvailableSlotModalModelComponent,
        BookedSlotModalModelComponent,
        PendingSlotModalModelComponent
      ]
    })
      .overrideComponent(TimelineSlotModalComponent, {
        set: { changeDetection: ChangeDetectionStrategy.Default }
      })
      .compileComponents();

    fixture = TestBed.createComponent(TimelineSlotModalComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should emit events when interacting with actions', () => {
    fixture.detectChanges();

    const slot: TimelineSlotDetails = {
      slotId: '1',
      serviceId: 'service',
      providerId: 'provider',
      serviceName: 'Service',
      label: 'Label',
      start: '08:00',
      end: '08:30',
      startTime: '2024-01-01T08:00:00Z',
      endTime: '2024-01-01T08:30:00Z',
      durationMinutes: 30,
      status: ReservationStatus.AVAILABLE,
      statusLabel: 'در دسترس',
      isMine: false,
      price: null
    };

    component.slot = slot;

    const closeSpy = jest.spyOn(component.close, 'emit');
    const addSpy = jest.spyOn(component.addToBasket, 'emit');

    component.onClose();
    component.onAddToBasket();

    expect(closeSpy).toHaveBeenCalled();
    expect(addSpy).toHaveBeenCalled();
  });
});
