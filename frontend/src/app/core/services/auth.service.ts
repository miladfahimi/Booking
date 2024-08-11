import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://192.168.0.16:8095/api/v1/auth';
  private tokenKey = 'authToken';  // Key to store the JWT token in local storage
  private userIdKey = 'userId';    // Key to store the userId in local storage

  constructor(private http: HttpClient, private router: Router) { }

  signIn(email: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/signin`, { email, password }).pipe(
      tap((response: any) => {
        console.log('Sign-in successful, redirecting to welcome page...', response);  // Debugging
        this.storeToken(response.token);  // Store the received JWT token
        this.storeUserId(response.id);     // Store the received userId (note: response.id)
        this.router.navigate(['/profile/welcome']).then(() => {
          console.log('Navigation to welcome page complete.');
          this.router.navigate(['/profile/welcome']);  // Redirect to the welcome page
        });
      })
    );
  }

  signUp(username: string, email: string, password: string, phone: string, role: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/signup`, { username, email, password, phone, role });
  }

  signOut(): void {
    this.removeToken();
    this.removeUserId();
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
