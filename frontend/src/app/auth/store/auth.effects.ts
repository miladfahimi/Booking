import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, mergeMap } from 'rxjs/operators';
import * as AuthActions from './auth.actions';
import { AuthService } from '../../core/services/auth.service';
import { SignUpReq } from './auth.models';

@Injectable()
export class AuthEffects {
  constructor(
    private actions$: Actions,
    private authService: AuthService
  ) { }

  signIn$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.signIn),
      mergeMap((action) =>
        this.authService.signIn(action.email, action.password).pipe(
          map((user) => AuthActions.signInSuccess({ user })),
          catchError((error) => of(AuthActions.signInFailure({ error })))
        )
      )
    )
  );

  signUp$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.signUp),
      mergeMap((action: SignUpReq) =>
        this.authService.signUp(action).pipe(
          map((user) => AuthActions.signUpSuccess({ user })),
          catchError((error) => of(AuthActions.signUpFailure({ error })))
        )
      )
    )
  );

  signOut$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.signOut),
      map(() => {
        this.authService.signOut();
        return AuthActions.signOutSuccess();
      }),
      catchError((error) => of(AuthActions.signOutFailure({ error })))
    )
  );
}
