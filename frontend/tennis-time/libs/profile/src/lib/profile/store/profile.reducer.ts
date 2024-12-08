import { createReducer, on } from '@ngrx/store';
import * as ProfileActions from './profile.actions';
import { LoadingStatus, UserProfileDTO } from '../types';

export interface ProfileState {
  profile: UserProfileDTO | null;
  loadingStatus: LoadingStatus;
  error: any;
}

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
  on(ProfileActions.loadProfileSuccess, (state, { profile }) => ({
    ...state,
    profile,
    loadingStatus: { loading: false, loaded: true },
    error: null,
  })),
  on(ProfileActions.loadProfileFailure, (state, { error }) => ({
    ...state,
    error,
    loadingStatus: { loading: false, loaded: true },
  }))
);
