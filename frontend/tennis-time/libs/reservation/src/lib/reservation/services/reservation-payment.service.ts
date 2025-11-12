import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentInitiationPayload, PaymentInitiationResult } from '../types/reservation-payment.types';

@Injectable({
  providedIn: 'root',
})
export class ReservationPaymentService {
  private readonly host = window.location.hostname;
  private readonly baseUrl = `http://${this.host}:8083/api/v1/portal/user`;

  constructor(private readonly http: HttpClient) { }

  initiatePayment(payload: PaymentInitiationPayload): Observable<PaymentInitiationResult> {
    const url = `${this.baseUrl}/payments/create`;
    return this.http.post<PaymentInitiationResult>(url, payload);
  }
}
