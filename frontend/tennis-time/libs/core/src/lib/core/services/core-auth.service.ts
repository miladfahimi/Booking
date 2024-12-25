import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { SignUpReq } from '@tennis-time/auth';

@Injectable({
  providedIn: 'root'
})
export class CoreAuthService {
  private baseUrl = 'http://192.168.0.16:8082/api/v1/auth';
  private tokenKey = 'authToken';  // Key to store the JWT token in local storage
  private userIdKey = 'userId';    // Key to store the userId in local storage

  constructor(private http: HttpClient, private router: Router) { }

  signIn(email: string, password: string, deviceModel: string, os: string, browser: string): Observable<any> {
    // Include deviceModel, os, and browser in the sign-in request
    return this.http.post(`${this.baseUrl}/signin`, { email, password, deviceModel, os, browser }).pipe(
      tap((response: any) => {
        console.log('Sign-in successful, redirecting to book page...', response);  // Debugging
        this.storeToken(response.token);  // Store the received JWT token
        this.storeUserId(response.id);     // Store the received userId (note: response.id)
        this.router.navigate(['/profile/welcome']).then(() => {
          console.log('Navigation to book page complete.');
        });
      })
    );
  }

  signUp(signUpReq: SignUpReq): Observable<any> {
    return this.http.post(`${this.baseUrl}/signup`, signUpReq);
  }

  signOut(): Observable<void> {
    this.removeToken();
    this.removeUserId();

    this.router.navigate(['/auth/signin'])
      .then(success => {
        if (success) {
          console.log('Navigation to /auth/signin was successful!');
        } else {
          console.error('Navigation to /auth/signin failed!');
        }
      })
      .catch(err => {
        console.error('Navigation error:', err);
      });

    return of();  // Return an observable of void
  }

  private storeToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  private storeUserId(userId: string): void {
    localStorage.setItem(this.userIdKey, userId);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getUserId(): string | null {
    return localStorage.getItem(this.userIdKey);
  }

  private removeToken(): void {
    localStorage.removeItem(this.tokenKey);
  }

  private removeUserId(): void {
    localStorage.removeItem(this.userIdKey);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
