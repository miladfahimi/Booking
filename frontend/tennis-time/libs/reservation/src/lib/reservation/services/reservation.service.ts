import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProviderDTO, ServiceDTO, SlotDTO } from '../types'; // Adjust the path according to your project structure

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  private host = window.location.hostname;
  private baseUrl = `http://${this.host}:8083/api/v1`;

  constructor(private http: HttpClient) { }

  getSlotsByServiceAndDate(serviceId: string, date: string): Observable<ServiceDTO> {
    return this.http.get<ServiceDTO>(`${this.baseUrl}/portal/user/services/${serviceId}/slots/${date}`);
  }
  getProvidersWithServices(): Observable<ProviderDTO[]> {
    return this.http.get<ProviderDTO[]>(`${this.baseUrl}/portal/user/providers-with-services`);
  }
}
