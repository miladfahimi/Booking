import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserProfileDTO, UserInitializationRequestDTO, UserInitializationResponseDTO } from '../types';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  private host = window.location.hostname;
  // Adjust the base path if your profile service exposes a different prefix.
  private baseUrl = `http://${this.host}:8084/api/v1`;

  constructor(private http: HttpClient) { }

  /** Existing: fetch profile by id (your current endpoint) */
  getProfileById(userId: string): Observable<UserProfileDTO> {
    return this.http.get<UserProfileDTO>(`${this.baseUrl}/user/profiles/${userId}`);
  }

  /** New: POST /user/initialize */
  initializeUser(payload: UserInitializationRequestDTO) {
    const url = `${this.baseUrl}/user/initialize`;
    console.log('[ProfileService] POST', url, payload);
    return this.http.post<UserInitializationResponseDTO>(url, payload);
  }


  /** New: GET /user/initialize/{userId} */
  getInitialization(userId: string): Observable<UserInitializationResponseDTO> {
    return this.http.get<UserInitializationResponseDTO>(`${this.baseUrl}/user/initialize/${userId}`);
  }
}
