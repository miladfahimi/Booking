import { ChangeDetectionStrategy } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimelineComponent } from './timeline.component';␊
import { ReservationStatus, ServiceDTO, SlotDTO } from '../../../../types';

describe('TimelineComponent', () => {
  let component: TimelineComponent;
  let fixture: ComponentFixture<TimelineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TimelineComponent]
    })
      .overrideComponent(TimelineComponent, {
        set: { changeDetection: ChangeDetectionStrategy.Default }
      })
      .compileComponents();

    fixture = TestBed.createComponent(TimelineComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should map service slots into timeline columns', () => {
    const services: ServiceDTO[] = [
      {
        id: 'service-1',
        name: 'Court 1',
        type: 'type',
        availability: true,
        providerId: 'provider-1',
        startTime: '08:00:00',
        endTime: '10:00:00',
        slotDuration: 30,
        slotGapDuration: null,
        tags: [],
        price: 0,
        maxCapacity: 4,
        slots: [],
        slotCount: 0,
        selected: false
      }
    ];

    const slotsByService: Record<string, SlotDTO[]> = {
      'service-1': [
        {
          slotId: 'slot-1',
          time: '08:00:00',
          endTime: '08:30:00',
          status: ReservationStatus.AVAILABLE,
          price: 0,
          capacity: 4,
          reservedBy: null,
          selected: false
        }
      ]
    };

    component.services = services;
    component.slotsByService = slotsByService;

    fixture.detectChanges();

    const columns = component.columns;
    expect(columns.length).toBe(1);
    expect(columns[0].slots.length).toBe(1);
    expect(columns[0].slots[0].durationMinutes).toBe(30);
    expect(columns[0].slots[0].status).toBe('available');
  });
});