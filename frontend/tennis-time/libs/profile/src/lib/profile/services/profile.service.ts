import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CoreAuthService } from '@tennis-time/core';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private baseUrl = 'http://192.168.0.16:8099/api/v1/user';

  constructor(private http: HttpClient, private coreAuthService: CoreAuthService) { }

  initializeUser(): Observable<any> {
    const userId = this.coreAuthService.getUserId();  // Retrieve the userId from AuthService
    console.log('Retrieved userId:', userId);  // Debugging

    if (!userId) {
      throw new Error('User ID is missing.');
    }

    return this.http.get(`${this.baseUrl}/initialize/${userId}`);
  }
}
