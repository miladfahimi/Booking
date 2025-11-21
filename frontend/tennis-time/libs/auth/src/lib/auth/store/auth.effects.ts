import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, exhaustMap } from 'rxjs/operators';
import * as AuthActions from './auth.actions';
import { CoreAuthService } from '@tennis-time/core';

@Injectable()
export class AuthEffects {
  constructor(
    private actions$: Actions,
    private coreAuthService: CoreAuthService
  ) { }

  signIn$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.signIn),
      exhaustMap((action) =>
        this.coreAuthService
          .signIn(action.email, action.password, action.deviceModel, action.os, action.browser, action.rememberMe)
          .pipe(
            map((user) => AuthActions.signInSuccess({ user })),
            catchError((error) =>
              of(AuthActions.signInFailure({ error: error?.error ?? error }))
            )
          )
      )
    )
  );

  signUp$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.signUp),
      exhaustMap((action) =>
        this.coreAuthService.signUp(action).pipe(
          map((user) => AuthActions.signUpSuccess({ user })),
          catchError((error) =>
            of(AuthActions.signUpFailure({ error: error?.error ?? error }))
          )
        )
      )
    )
  );

  signOut$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.signOut),
      map(() => {
        this.coreAuthService.signOut();
        return AuthActions.signOutSuccess();
      }),
      catchError((error) => of(AuthActions.signOutFailure({ error: error?.error ?? error })))
    )
  );

  requestOtpSms$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.requestOtpSms),
      exhaustMap((action) =>
        this.coreAuthService.sendOtpSms(action.phone).pipe(
          map(() => AuthActions.requestOtpSmsSuccess()),
          catchError((error) =>
            of(AuthActions.requestOtpSmsFailure({ error: error?.error ?? error }))
          )
        )
      )
    )
  );

  signInWithOtp$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.signInWithOtp),
      exhaustMap((action) =>
        this.coreAuthService
          .verifyOtpAndSignIn(action.phone, action.otp, action.deviceModel, action.os, action.browser)
          .pipe(
            map((user) => AuthActions.signInSuccess({ user })),
            catchError((error) =>
              of(AuthActions.signInFailure({ error: error?.error ?? error }))
            )
          )
      )
    )
  );
}
