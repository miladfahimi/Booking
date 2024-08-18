import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CoreAuthService } from '@tennis-time/core';

import { ReservationRoutingModule } from './reservation-routing.module';
import { BookComponent } from './pages/book/book.component';
import { ReservationService } from './services/reservation.service';
import {SharedModule} from "@tennis-time/shared";
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { reservationReducer } from './store/reservation.reducer';
import { ReservationEffects } from './store/reservation.effects';

@NgModule({
  declarations: [
    BookComponent
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
    ReservationService,  // Ensure ReservationService is provided here if not provided in root
    CoreAuthService,
  ]
})
export class ReservationModule { }
