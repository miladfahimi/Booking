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
import {
  ReservationItemsComponent
} from "./pages/reservation/container/items/reservation-items.component";
import { ReservationHoursComponent } from "./pages/reservation/container/hours/reservation-hours.component";
import {
  ReservationCalendarMobileComponent
} from "./pages/reservation/container/calendar/calendar-mobile/reservation-calendar-mobile.component";
import { TimelineComponent } from './pages/reservation/container/timeline/timeline.component';
import { TimelineSlotAvailableComponent } from './pages/reservation/container/timeline/timeline-slots/timeline-slot-available/timeline-slot-avalilable.component';
import { TimelineSlotBasketComponent } from './pages/reservation/container/timeline/timeline-slots/basket-slot/timeline-slot-basket.component';
import { TimelineSlotBasketMineComponent } from './pages/reservation/container/timeline/timeline-slots/basket-mine-slot/timeline-slot-basket-mine.component';
import { TimelineSlotBookedComponent } from './pages/reservation/container/timeline/timeline-slots/booked-slot/timeline-slot-booked.component';
import { TimelineSlotBookedMineComponent } from './pages/reservation/container/timeline/timeline-slots/booked-mine-slot/timeline-slot-booked-mine.component';
import { TimelineSlotPendingComponent } from './pages/reservation/container/timeline/timeline-slots/pending-slot/timeline-slot-pending.component';
import { TimelineSlotMaintenanceComponent } from './pages/reservation/container/timeline/timeline-slots/maintenance-slot/timeline-slot-maintenance.component';
import { TimelineSlotModalComponent } from './pages/reservation/container/timeline/tileline-slot-modals/timeline-slot-modals.component';
import { ReservationBasketComponent } from './pages/reservation/container/basket/reservation-basket.component';
import { environment } from '@tennis-time/environment';
import { RESERVATION_FEATURE_CONFIG } from './config/reservation-feature-config.token';
import { ReservationCheckoutService } from './services/reservation-checkout.service';
import { MineSlotModalModelComponent } from './pages/reservation/container/timeline/tileline-slot-modals/mine-slot-modal/mine-slot-modal.model.component';
import { AvailableSlotModalModelComponent } from './pages/reservation/container/timeline/tileline-slot-modals/available-slot-modal/available-slot-modal.model.component';
import { BookedSlotModalModelComponent } from './pages/reservation/container/timeline/tileline-slot-modals/booked-slot-modal/booked-slot-modal.model.component';
import { PendingSlotModalModelComponent } from './pages/reservation/container/timeline/tileline-slot-modals/pending-slot-modal/pending-slot-modal.model.component';
import { ReservationBasketModalComponent } from './pages/reservation/container/basket/reservation-basket-modal.component';
import { PaymentModule, PAYMENT_FEATURE_CONFIG } from '@tennis-time/payment';

@NgModule({
  declarations: [
    ReservationCalendarMobileComponent,
    ReservationContainerComponent,
    ReservationCalendarDesktopComponent,
    ReservationItemsComponent,
    ReservationHoursComponent,
    TimelineComponent,
    TimelineSlotAvailableComponent,
    TimelineSlotBasketComponent,
    TimelineSlotBasketMineComponent,
    TimelineSlotBookedComponent,
    TimelineSlotBookedMineComponent,
    TimelineSlotPendingComponent,
    TimelineSlotMaintenanceComponent,
    TimelineSlotModalComponent,
    ReservationBasketComponent,
    ReservationBasketModalComponent,
    MineSlotModalModelComponent,
    AvailableSlotModalModelComponent,
    BookedSlotModalModelComponent,
    PendingSlotModalModelComponent,
  ],
  imports: [
    FormsModule,
    CommonModule,
    ReservationRoutingModule,
    NgOptimizedImage,
    SharedModule,
    PaymentModule,
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
    {
      provide: PAYMENT_FEATURE_CONFIG,
      useValue: {
        useMockPaymentGateway: environment.mockPaymentPageEnabled
      }
    },
    ReservationCheckoutService
  ]
})
export class ReservationModule { }
