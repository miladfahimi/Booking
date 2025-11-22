import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '@tennis-time/shared';
import { PaymentMockPaymentComponent } from './mock-payment/payment-mock-payment.component';

@NgModule({
  declarations: [PaymentMockPaymentComponent],
  imports: [CommonModule, SharedModule],
  exports: [PaymentMockPaymentComponent]
})
export class PaymentModule { }
