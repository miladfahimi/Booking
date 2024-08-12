import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {EMPTY, of} from 'rxjs';
import { catchError, map, mergeMap } from 'rxjs/operators';
import * as AuthActions from './auth.actions';
import { CoreAuthService } from '@tennis-time/core';
import { SignUpReq } from './auth.models';

@Injectable()
export class AuthEffects {
  constructor(
    private actions$: Actions,
    private coreAuthService: CoreAuthService
  ) { }


  signIn$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.signIn),
      mergeMap((action) =>
        this.coreAuthService.signIn(action.email, action.password).pipe(
          map((user) => AuthActions.signInSuccess({ user })),
          catchError((error) => of(AuthActions.signInFailure({ error })))
        )
      )
    )
  );

  signUp$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.signUp),
      mergeMap((action: SignUpReq) => {
        console.log('signUp action received:', action);  // Debugging
        return this.coreAuthService.signUp(action).pipe(
          map((user) => {
            console.log('signUp success:', user);  // Debugging
            return AuthActions.signUpSuccess({ user });
          }),
          catchError((error) => {
            console.error('signUp error:', error);  // Debugging
            return of(AuthActions.signUpFailure({ error }));
          })
        );
      })
    )
  );

  signOut$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.signOut),
      map(() => {
        console.log('signOut action received');  // Debugging
        this.coreAuthService.signOut();
        return AuthActions.signOutSuccess();
      }),
      catchError((error) => {
        console.error('signOut error:', error);  // Debugging
        return of(AuthActions.signOutFailure({ error }));
      })
    )
  );
}
