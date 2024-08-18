import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserReservationDTO } from '../types';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  private baseUrl = 'http://192.168.0.16:8099/api/v1'; // Update with your API base URL

  constructor(private http: HttpClient) {}

  getReservationById(userId: string): Observable<UserReservationDTO> {
    return this.http.get<UserReservationDTO>(`${this.baseUrl}/user/reservations/${userId}`);
  }
}
