import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserProfileDTO } from '../types';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  private host = window.location.hostname;
  private baseUrl = `http://${this.host}:8084/api/v1`;

  constructor(private http: HttpClient) { }

  getProfileById(userId: string): Observable<UserProfileDTO> {
    return this.http.get<UserProfileDTO>(`${this.baseUrl}/user/profiles/${userId}`);
  }
}
