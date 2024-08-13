import { createAction, props } from '@ngrx/store';
import { UserProfileDTO } from '../types';

export const loadProfile = createAction(
  '[Profile] Load Profile',
  props<{ userId: string }>()
);

export const loadProfileSuccess = createAction(
  '[Profile] Load Profile Success',
  props<{ profile: UserProfileDTO }>()
);

export const loadProfileFailure = createAction(
  '[Profile] Load Profile Failure',
  props<{ error: any }>()
);
