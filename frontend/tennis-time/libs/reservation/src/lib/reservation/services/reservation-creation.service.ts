import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateReservationPayload, ReservationStatus, UserReservationDTO } from '../types';
import { ReservationBulkRequestItem, ReservationSummary } from '../types/reservation-basket.types';

@Injectable({
  providedIn: 'root',
})
export class ReservationCreationService {
  private readonly host = window.location.hostname;
  private readonly baseUrl = `http://${this.host}:8083/api/v1`;

  constructor(private readonly http: HttpClient) { }

  createReservation(payload: CreateReservationPayload): Observable<UserReservationDTO> {
    const url = `${this.baseUrl}/portal/user/reservations`;
    return this.http.post<UserReservationDTO>(url, payload);
  }

  createReservationsBulk(payload: ReservationBulkRequestItem[]): Observable<ReservationSummary[]> {
    const url = `${this.baseUrl}/portal/user/reservations/bulk`;
    return this.http.post<ReservationSummary[]>(url, payload);
  }

  updateReservationStatus(reservationId: string, status: ReservationStatus): Observable<ReservationSummary> {
    const url = `${this.baseUrl}/portal/user/reservations/${reservationId}/status`;
    return this.http.put<ReservationSummary>(url, null, { params: { status } });
  }
}