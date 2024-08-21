import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ServiceDTO } from '../types';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  private baseUrl = 'http://192.168.0.16:8089/api/v1'; // Update with your API base URL

  constructor(private http: HttpClient) {}

  getSlotsByServiceAndDate(serviceId: string, date: string): Observable<ServiceDTO> {
    return this.http.get<ServiceDTO>(`${this.baseUrl}/portal/user/services/${serviceId}/slots/${date}`);
  }
}
