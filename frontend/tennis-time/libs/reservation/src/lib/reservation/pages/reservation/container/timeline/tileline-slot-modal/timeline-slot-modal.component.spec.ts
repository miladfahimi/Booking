import { ChangeDetectionStrategy } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimelineSlotModalComponent, TimelineSlotDetails } from './timeline-slot-modal.component';

describe('TimelineSlotModalComponent', () => {
  let component: TimelineSlotModalComponent;
  let fixture: ComponentFixture<TimelineSlotModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TimelineSlotModalComponent]
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
      serviceName: 'Service',
      label: 'Label',
      start: '08:00',
      end: '08:30',
      durationMinutes: 30,
      status: 'available',
      statusLabel: 'در دسترس'
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
