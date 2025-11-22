import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '@tennis-time/core';
import { ReservationContainerComponent } from "./pages/reservation/container/reservation-container.component";
import { PaymentMockPaymentComponent } from '@tennis-time/payment';


const routes: Routes = [
  { path: 'payment/mock/:paymentId', component: PaymentMockPaymentComponent },
  { path: 'book', component: ReservationContainerComponent, canActivate: [AuthGuard] },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReservationRoutingModule { }
