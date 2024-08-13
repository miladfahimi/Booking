import { createReducer, on } from '@ngrx/store';
import * as ProfileActions from './profile.actions';
import { LoadingStatus, UserProfileDTO } from '../types';

export interface ProfileState {
  profile: UserProfileDTO | null;
  loadingStatus: LoadingStatus;
  error: any;
}

// Updated initialState with the correct type for profile
export const initialState: ProfileState = {
  profile: null,
  loadingStatus: { loading: false, loaded: false },
  error: null,
};

export const profileReducer = createReducer(
  initialState,
  on(ProfileActions.loadProfile, (state) => ({
    ...state,
    loadingStatus: { loading: true, loaded: false },
    error: null,
  })),
  on(ProfileActions.loadProfileSuccess, (state, { profile }) => {
    const newState = {
      ...state,
      profile,  // Assign the loaded profile
      loadingStatus: { loading: false, loaded: true },
      error: null,
    };

    console.log('New State:', JSON.stringify(newState)); // Log the new state as a JSON string

    return newState;
  }),
  on(ProfileActions.loadProfileFailure, (state, { error }) => ({
    ...state,
    error,
    loadingStatus: { loading: false, loaded: true },
  }))
);
