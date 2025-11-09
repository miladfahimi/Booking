import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateReservationPayload, UserReservationDTO } from '../types';

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
}