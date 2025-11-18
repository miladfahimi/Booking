import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProviderDTO, ServiceDTO } from '../types';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  private host = window.location.hostname;
  private baseUrl = `http://${this.host}:8083/api/v1`;

  constructor(private http: HttpClient) { }

  getSlotsByServiceAndDate(serviceId: string, date: string, currentUserId?: string | null): Observable<ServiceDTO> {
    const params = this.buildUserParam(currentUserId);
    return this.http.get<ServiceDTO>(`${this.baseUrl}/portal/user/services/${serviceId}/slots/${date}`, { params });
  }

  getSlotsByDate(date: string, currentUserId?: string | null): Observable<ServiceDTO[]> {
    const params = this.buildUserParam(currentUserId);
    return this.http.get<ServiceDTO[]>(`${this.baseUrl}/portal/user/services/slots/${date}`, { params });
  }

  private buildUserParam(currentUserId?: string | null): HttpParams | undefined {
    if (!currentUserId) {
      return undefined;
    }
    return new HttpParams().set('currentUserId', currentUserId);
  }
  getProvidersWithServices(): Observable<ProviderDTO[]> {
    return this.http.get<ProviderDTO[]>(`${this.baseUrl}/portal/user/providers-with-services`);
  }
}
