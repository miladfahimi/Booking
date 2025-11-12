import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CoreAuthService } from '@tennis-time/core';

import { ReservationRoutingModule } from './reservation-routing.module';
import { ReservationService } from './services/reservation.service';
import { SharedModule } from "@tennis-time/shared";
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { reservationReducer } from './store/reservation.reducer';
import { ReservationEffects } from './store/reservation.effects';
import { ReservationContainerComponent } from "./pages/reservation/container/reservation-container.component";
import { ReservationCalendarDesktopComponent } from "./pages/reservation/container/calendar/calendar-desktop/reservation-calendar-desktop.component";
import { ReservationSlotsTableComponent } from "./pages/reservation/container/table/reservation-slots-table.component";
import {
  ReservationItemsComponent
} from "./pages/reservation/container/items/reservation-items.component";
import { ReservationHoursComponent } from "./pages/reservation/container/hours/reservation-hours.component";
import {
  ReservationCalendarMobileComponent
} from "./pages/reservation/container/calendar/calendar-mobile/reservation-calendar-mobile.component";
import { TimelineComponent } from './pages/reservation/container/timeline/timeline.component';
import { TimelineSlotModalComponent } from './pages/reservation/container/timeline/tileline-slot-modal/timeline-slot-modal.component';
import { ReservationBasketComponent } from './pages/reservation/container/basket/reservation-basket.component';
import { ReservationMockPaymentComponent } from './pages/mock-payment/reservation-mock-payment.component';
import { environment } from '@tennis-time/environment';
import { RESERVATION_FEATURE_CONFIG } from './config/reservation-feature-config.token';
import { MockPaymentSessionService } from './services/mock/mock-payment-session.service';
import { ReservationCheckoutService } from './services/reservation-checkout.service';

@NgModule({
  declarations: [
    ReservationCalendarMobileComponent,
    ReservationContainerComponent,
    ReservationCalendarDesktopComponent,
    ReservationItemsComponent,
    ReservationHoursComponent,
    ReservationSlotsTableComponent,
    TimelineComponent,
    TimelineSlotModalComponent,
    ReservationBasketComponent,
    ReservationMockPaymentComponent,
  ],
  imports: [
    FormsModule,
    CommonModule,
    ReservationRoutingModule,
    NgOptimizedImage,
    SharedModule,
    StoreModule.forFeature('reservation', reservationReducer),
    EffectsModule.forFeature([ReservationEffects])
  ],
  providers: [
    ReservationService,
    CoreAuthService,
    {
      provide: RESERVATION_FEATURE_CONFIG,
      useValue: {
        useMockPaymentGateway: environment.mockPaymentPageEnabled
      }
    },
    MockPaymentSessionService,
    ReservationCheckoutService
  ]
})
export class ReservationModule { }
