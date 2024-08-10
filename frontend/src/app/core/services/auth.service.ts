import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';  // Import HttpParams
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8095/api/v1/auth';
  private tokenKey = 'authToken';  // Key to store the JWT token in local storage

  constructor(private http: HttpClient) {}

  signIn(email: string, password: string): Observable<any> {
    const params = new HttpParams().set('email', email).set('password', password);  // Use HttpParams

    return this.http.post(`${this.baseUrl}/signin`, { email, password }).pipe(
      tap((response: any) => this.storeToken(response.token))
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
