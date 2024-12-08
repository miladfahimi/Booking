import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ProfileState } from './profile.reducer';

export const selectProfileState = createFeatureSelector<ProfileState>('profile');

// Selector to get user profile data
export const selectProfile = createSelector(
  selectProfileState,
  (state: ProfileState) => state.profile
);
export const selectProfileLoading = createSelector(
  selectProfileState,
  (state: ProfileState) => state.loadingStatus.loading
);

export const selectProfileError = createSelector(
  selectProfileState,
  (state: ProfileState) => state.error
);
