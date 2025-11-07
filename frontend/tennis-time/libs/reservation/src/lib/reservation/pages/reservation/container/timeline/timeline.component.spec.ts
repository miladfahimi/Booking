import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationTimelineComponent } from './timeline.component';

describe('ReservationTimelineComponent', () => {
  let component: ReservationTimelineComponent;
  let fixture: ComponentFixture<ReservationTimelineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReservationTimelineComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(ReservationTimelineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should calculate slot height based on duration', () => {
    const [firstColumn] = component.demoColumns;
    const [slot] = firstColumn.slots;

    expect(component.getSlotHeight(slot)).toBe(slot.durationMinutes * component.pixelsPerMinute);
  });

  it('should calculate slot offset from start hour', () => {
    const [firstColumn] = component.demoColumns;
    const [slot] = firstColumn.slots;
    const [hours, minutes] = slot.start.split(':').map(Number);
    const expectedMinutesFromStart = (hours - component.startHour) * 60 + minutes;

    expect(component.getSlotOffset(slot)).toBe(expectedMinutesFromStart * component.pixelsPerMinute);
  });
});
