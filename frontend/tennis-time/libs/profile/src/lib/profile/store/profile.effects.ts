import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, mergeMap } from 'rxjs/operators';
import * as ProfileActions from './profile.actions';
import { ProfileService } from '../services/profile.service';
import { UserProfileDTO } from '../types';

@Injectable()
export class ProfileEffects {
  constructor(
    private actions$: Actions,
    private profileService: ProfileService
  ) {}

  loadProfile$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ProfileActions.loadProfile),
      mergeMap((action) =>
        this.profileService.getProfileById(action.userId).pipe(
          map((profile: UserProfileDTO) =>
            ProfileActions.loadProfileSuccess({ profile })
          ),
          catchError((error) =>
            of(ProfileActions.loadProfileFailure({ error }))
          )
        )
      )
    )
  );
}
