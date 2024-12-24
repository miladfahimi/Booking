import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserProfileDTO } from '../types';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  private baseUrl = 'http://49.12.109.90:8084/api/v1'; // Update with your API base URL

  constructor(private http: HttpClient) { }

  getProfileById(userId: string): Observable<UserProfileDTO> {
    return this.http.get<UserProfileDTO>(`${this.baseUrl}/user/profiles/${userId}`);
  }
}
