import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';  // Import HttpParams
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://192.168.0.16:8095/api/v1/auth';
  private tokenKey = 'authToken';  // Key to store the JWT token in local storage

  constructor(private http: HttpClient, private router: Router) { }

  signIn(email: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/signin`, { email, password }).pipe(
      tap((response: any) => {
        console.log('Sign-in successful, redirecting to welcome page...');  // Debugging
        this.storeToken(response.token);  // Store the received JWT token
        this.router.navigate(['/profile/welcome']).then(() => {
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
  }

  private storeToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  private removeToken(): void {
    localStorage.removeItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
