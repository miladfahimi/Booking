import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { CoreAuthService } from '@tennis-time/core';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private coreAuthService: CoreAuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.coreAuthService.getToken();
    const isRefreshRequest = req.url.includes('/refresh');
    const authReq = token
      ? req.clone({ headers: req.headers.set('Authorization', `Bearer ${token}`) })
      : req;

    return next.handle(authReq).pipe(
      catchError(err => {
        if (err.status === 401 && !isRefreshRequest && this.coreAuthService.getRefreshToken()) {
          return this.coreAuthService.refreshTokens().pipe(
            switchMap(tokens => {
              const retried = req.clone({
                headers: req.headers.set('Authorization', `Bearer ${tokens.accessToken}`)
              });
              return next.handle(retried);
            }),
            catchError(() => throwError(() => err))
          );
        }
        return throwError(() => err);
      })
    );
  }
}
