import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, mergeMap, tap } from 'rxjs/operators';
import * as ProfileActions from './profile.actions';
import { ProfileService } from '../services/profile.service';
import { UserProfileDTO, UserInitializationRequestDTO, UserInitializationResponseDTO } from '../types';
import { Router } from '@angular/router';

@Injectable()
export class ProfileEffects {
  constructor(
    private actions$: Actions,
    private profileService: ProfileService,
    private router: Router
  ) { }

  loadProfile$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ProfileActions.loadProfile),
      mergeMap((action) =>
        this.profileService.getProfileById(action.userId).pipe(
          map((profile: UserProfileDTO) =>
            ProfileActions.loadProfileSuccess({ profile })
          ),
          catchError((error) => {
            if (error?.status === 404) {
              return of(ProfileActions.loadProfileNotFound());
            }
            return of(ProfileActions.loadProfileFailure({ error }));
          })
        )
      )
    )
  );

  /** Initialize profile (POST /user/initialize) */
  initializeProfile$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ProfileActions.initializeProfile),
      mergeMap(({ payload }: { payload: UserInitializationRequestDTO }) =>
        this.profileService.initializeUser(payload).pipe(
          tap(() => console.log('[ProfileEffects] initializeUser API called')),
          map((response: UserInitializationResponseDTO) =>
            ProfileActions.initializeProfileSuccess({ response })
          ),
          catchError((error) => {
            if (error?.status === 409) {
              return of(ProfileActions.initializeProfileConflict());
            }
            return of(ProfileActions.initializeProfileFailure({ error }));
          })
        )
      )
    )
  );

  /** Navigate after success or conflict */
  afterInitialize$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(ProfileActions.initializeProfileSuccess),
        tap(() => this.router.navigate(['/profile/entry']))
      ),
    { dispatch: false }
  );
}
