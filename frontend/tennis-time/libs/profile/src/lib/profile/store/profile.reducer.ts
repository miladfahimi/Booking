import { createReducer, on } from '@ngrx/store';
import * as ProfileActions from './profile.actions';
import { UserProfileDTO, UserInitializationResponseDTO } from '../types';

export interface ProfileState {
  profile?: UserProfileDTO;
  initResponse?: UserInitializationResponseDTO;
  loading: boolean;
  error?: any;
  initialized: boolean;
}

export const initialState: ProfileState = {
  loading: false,
  initialized: false
};

export const profileReducer = createReducer(
  initialState,

  on(ProfileActions.loadProfile, (state) => ({ ...state, loading: true, error: undefined })),
  on(ProfileActions.loadProfileSuccess, (state, { profile }) => ({ ...state, loading: false, profile })),
  on(ProfileActions.loadProfileFailure, (state, { error }) => ({ ...state, loading: false, error })),

  on(ProfileActions.initializeProfile, (state) => ({ ...state, loading: true, error: undefined })),
  on(ProfileActions.initializeProfileSuccess, (state, { response }) => ({
    ...state,
    loading: false,
    initResponse: response,
    profile: response.userProfileDTO,
    initialized: true
  })),
  on(ProfileActions.initializeProfileConflict, (state) => ({
    ...state,
    loading: false,
    initialized: true
  })),
  on(ProfileActions.initializeProfileFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);
