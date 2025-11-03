// auth.reducer.ts
import { createReducer, on } from '@ngrx/store';
import * as AuthActions from './auth.actions';
import { LoadingStatus } from './auth.models';

export interface AuthState {
  user: any; // replace `any` with your user model
  error: string | null;
  isAuthenticated: boolean;
  loadingStatus: LoadingStatus;  // Use LoadingStatus model
}

export const initialState: AuthState = {
  user: null,
  error: null,
  isAuthenticated: false,
  loadingStatus: {
    loaded: false,
    loading: false,
  },
};

export const authReducer = createReducer(
  initialState,

  // Handle sign-in actions
  on(AuthActions.signIn, (state) => ({
    ...state,
    loadingStatus: { loaded: false, loading: true },
    error: null,
  })),

  on(AuthActions.signInSuccess, (state, { user }) => ({
    ...state,
    user,
    isAuthenticated: true,
    loadingStatus: { loaded: true, loading: false },
    error: null,
  })),

  on(AuthActions.signInFailure, (state, { error }) => ({
    ...state,
    error,
    isAuthenticated: false,
    loadingStatus: { loaded: false, loading: false },
  })),

  on(AuthActions.signUp, (state) => ({
    ...state,
    loadingStatus: { loaded: false, loading: true },
    error: null,
  })),

  on(AuthActions.signUpSuccess, (state, { user }) => ({
    ...state,
    user,
    isAuthenticated: true,
    loadingStatus: { loaded: true, loading: false },
    error: null,
  })),

  on(AuthActions.signUpFailure, (state, { error }) => ({
    ...state,
    error,
    isAuthenticated: false,
    loadingStatus: { loaded: false, loading: false },
  })),

  // Handle sign-out actions
  on(AuthActions.signOut, (state) => ({
    ...state,
    loadingStatus: { loaded: false, loading: true },
    error: null,
  })),

  on(AuthActions.signOutSuccess, (state) => ({
    ...state,
    user: null,
    isAuthenticated: false,
    loadingStatus: { loaded: true, loading: false },
    error: null,
  })),

  on(AuthActions.signOutFailure, (state, { error }) => ({
    ...state,
    error,
    loadingStatus: { loaded: false, loading: false },
  })),

  on(AuthActions.requestOtpSms, (state) => ({
    ...state,
    loadingStatus: { loaded: false, loading: true },
    error: null,
  })),

  on(AuthActions.requestOtpSmsSuccess, (state) => ({
    ...state,
    loadingStatus: { loaded: true, loading: false },
    error: null,
  })),

  on(AuthActions.requestOtpSmsFailure, (state, { error }) => ({
    ...state,
    error,
    loadingStatus: { loaded: false, loading: false },
  })),

  on(AuthActions.signInWithOtp, (state) => ({
    ...state,
    loadingStatus: { loaded: false, loading: true },
    error: null,
  }))
);
