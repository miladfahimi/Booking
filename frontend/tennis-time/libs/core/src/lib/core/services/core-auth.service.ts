import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { SignUpReq } from '@tennis-time/auth';

@Injectable({
  providedIn: 'root'
})
export class CoreAuthService {
  private host = window.location.hostname;
  private baseUrl = `http://${this.host}:8082/api/v1/auth`;
  private tokenKey = 'authToken';
  private userIdKey = 'userId';
  private refreshTokenKey = 'refreshToken';

  constructor(private http: HttpClient, private router: Router) { }

  signIn(email: string, password: string, deviceModel: string, os: string, browser: string, rememberMe: boolean): Observable<any> {
    return this.http.post(`${this.baseUrl}/signin`, { email, password, deviceModel, os, browser, rememberMe }).pipe(
      tap((response: any) => {
        this.storeTokens(response.token, response.refreshToken, rememberMe);
        this.storeUserId(response.id, rememberMe);
        this.router.navigate(['/profile/welcome']).then(() => {
          console.log('Navigation to book page complete.');
        });
      })
    );
  }

  signUp(signUpReq: SignUpReq): Observable<any> {
    return this.http.post(`${this.baseUrl}/signup`, signUpReq).pipe(
      tap((response: any) => {
        this.storeTokens(response.token, response.refreshToken, true);
        this.storeUserId(response.id, true);
      })
    );
  }

  signOut(): Observable<void> {
    this.removeToken();
    this.removeUserId();
    this.removeRefreshToken();

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

    return of();
  }

  sendOtpSms(phone: string) {
    const otpBase = `http://${this.host}:8082/api/v1/otp`;
    return this.http.post(`${otpBase}/login/send-sms?phone=${encodeURIComponent(phone)}`, {});
  }

  verifyOtpAndSignIn(phone: string, otp: string, deviceModel: string, os: string, browser: string) {
    const otpBase = `http://${this.host}:8082/api/v1/otp`;
    return this.http.post(`${otpBase}/login-sms?phone=${encodeURIComponent(phone)}&otp=${encodeURIComponent(otp)}`, { deviceModel, os, browser }).pipe(
      tap((response: any) => {
        this.storeTokens(response.token, response.refreshToken, true);
        this.storeUserId(response.id, true);
        this.router.navigate(['/profile/welcome']);
      })
    );
  }

  refreshTokens(): Observable<{ accessToken: string; refreshToken: string }> {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken) {
      return throwError(() => new Error('No refresh token available'));
    }
    return this.http.post<{ accessToken: string; refreshToken: string }>(`${this.baseUrl}/refresh`, { refreshToken }).pipe(
      tap(response => this.storeTokens(response.accessToken, response.refreshToken, true))
    );
  }

  private storeTokens(token: string, refreshToken?: string, rememberMe: boolean = true): void {
    const tokenStore = rememberMe ? localStorage : sessionStorage;
    tokenStore.setItem(this.tokenKey, token);
    if (refreshToken) {
      tokenStore.setItem(this.refreshTokenKey, refreshToken);
    }
  }

  private storeUserId(userId: string, rememberMe: boolean = true): void {
    const tokenStore = rememberMe ? localStorage : sessionStorage;
    tokenStore.setItem(this.userIdKey, userId);
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.tokenKey) || localStorage.getItem(this.tokenKey);
  }

  getRefreshToken(): string | null {
    return sessionStorage.getItem(this.refreshTokenKey) || localStorage.getItem(this.refreshTokenKey);
  }

  getUserId(): string | null {
    return sessionStorage.getItem(this.userIdKey) || localStorage.getItem(this.userIdKey);
  }

  private removeToken(): void {
    localStorage.removeItem(this.tokenKey);
    sessionStorage.removeItem(this.tokenKey);
  }

  private removeRefreshToken(): void {
    localStorage.removeItem(this.refreshTokenKey);
    sessionStorage.removeItem(this.refreshTokenKey);
  }

  private removeUserId(): void {
    localStorage.removeItem(this.userIdKey);
    sessionStorage.removeItem(this.userIdKey);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
