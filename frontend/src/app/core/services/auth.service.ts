import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8095/api/v1/auth';
  private tokenKey = 'authToken';

  constructor(private http: HttpClient) {}

  signIn(email: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/signin`, { email, password }).pipe(
      tap((response: any) => this.storeToken(response.token))
    );
  }

  signUp(username: string, email: string, password: string, phone: string, role: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/signup`, { username, email, password, phone, role });
  }


  private storeToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  signOut(): void {
    this.removeToken();
  }

  private removeToken(): void {
    localStorage.removeItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
