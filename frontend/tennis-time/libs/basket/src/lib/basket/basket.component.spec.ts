import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { CoreAuthService } from '@tennis-time/core';
import { ReservationBasketApiService } from '@tennis-time/reservation';
import { BasketComponent } from './basket.component';

describe('BasketComponent', () => {
  let component: BasketComponent;
  let fixture: ComponentFixture<BasketComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BasketComponent, RouterTestingModule],
      providers: [
        { provide: CoreAuthService, useValue: { getUserId: () => 'user-1' } },
        {
          provide: ReservationBasketApiService,
          useValue: {
            getBasket: () => of([]),
            removeItem: () => of(void 0),
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(BasketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});