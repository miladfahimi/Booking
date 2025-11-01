import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ProfileState } from './profile.reducer';

export const selectProfileState = createFeatureSelector<ProfileState>('profile');

export const selectProfile = createSelector(
  selectProfileState,
  (state: ProfileState) => state.profile ?? null
);

export const selectProfileLoading = createSelector(
  selectProfileState,
  (state: ProfileState) => state?.loading === true
);

export const selectProfileError = createSelector(
  selectProfileState,
  (state: ProfileState) => state.error
);

/** New selectors for initialization flow */
export const selectInitializationResponse = createSelector(
  selectProfileState,
  (state: ProfileState) => (state as any).initResponse
);

export const selectInitialized = createSelector(
  selectProfileState,
  (state: ProfileState) =>
    (state as any).initialized === true ||
    Boolean(state?.profile?.id || (state as any).initResponse?.userProfilesInitiated)
);

export const selectInitializationLoading = createSelector(
  selectProfileState,
  (state: ProfileState) =>
    // if you keep a separate loading flag for initialize, prefer it; else reuse loadingStatus
    (state as any).initLoading ?? state?.loading === true
);
