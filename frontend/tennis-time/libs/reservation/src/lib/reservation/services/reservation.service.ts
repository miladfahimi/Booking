import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  private baseUrl = 'http://192.168.0.16:8089/api/v1'; // Update with your API base URL

  constructor(private http: HttpClient) {}

  getSlotsByServiceAndDate(serviceId: string, date: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/portal/user/services/${serviceId}/slots/${date}`);
  }
}
