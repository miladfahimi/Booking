import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommonModule } from '@angular/common';

import { ReservationSlotsTableComponent } from './reservation-slots-table.component';

describe('ReservationSlotsTableComponent', () => {
  let component: ReservationSlotsTableComponent;
  let fixture: ComponentFixture<ReservationSlotsTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommonModule],
      declarations: [ReservationSlotsTableComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(ReservationSlotsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
