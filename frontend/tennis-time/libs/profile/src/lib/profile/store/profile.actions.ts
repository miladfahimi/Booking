import { createAction, props } from '@ngrx/store';
import { UserInitializationRequestDTO, UserInitializationResponseDTO, UserProfileDTO } from '../types';

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

export const initializeProfile = createAction(
  '[Profile] Initialize Profile',
  props<{ payload: UserInitializationRequestDTO }>()
);

export const initializeProfileSuccess = createAction(
  '[Profile] Initialize Profile Success',
  props<{ response: UserInitializationResponseDTO }>()
);

export const initializeProfileConflict = createAction(
  '[Profile] Initialize Profile Conflict',
);

export const initializeProfileFailure = createAction(
  '[Profile] Initialize Profile Failure',
  props<{ error: any }>()
);
