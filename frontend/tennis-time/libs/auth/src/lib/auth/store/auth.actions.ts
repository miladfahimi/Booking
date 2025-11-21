import { createAction, props } from '@ngrx/store';
import { SignUpReq } from './auth.models';

export const signIn = createAction(
  '[Auth] Sign In',
  props<{ email: string; password: string; deviceModel: string; os: string; browser: string; rememberMe: boolean }>()
);

export const signInSuccess = createAction(
  '[Auth] Sign In Success',
  props<{ user: any }>() // replace `any` with your user model
);

export const signInFailure = createAction(
  '[Auth] Sign In Failure',
  props<{ error: any }>()
);

export const signUp = createAction(
  '[Auth] Sign Up',
  props<SignUpReq>()
);

export const signUpSuccess = createAction(
  '[Auth] Sign Up Success',
  props<{ user: any }>() // replace `any` with your user model
);

export const signUpFailure = createAction(
  '[Auth] Sign Up Failure',
  props<{ error: any }>()
);

export const signOut = createAction('[Auth] Sign Out');

export const signOutSuccess = createAction('[Auth] Sign Out Success');

export const signOutFailure = createAction(
  '[Auth] Sign Out Failure',
  props<{ error: any }>()
);

export const requestOtpSms = createAction(
  '[Auth] Request OTP SMS',
  props<{ phone: string }>()
);

export const requestOtpSmsSuccess = createAction('[Auth] Request OTP SMS Success');

export const requestOtpSmsFailure = createAction(
  '[Auth] Request OTP SMS Failure',
  props<{ error: any }>()
);

export const signInWithOtp = createAction(
  '[Auth] Sign In With OTP',
  props<{ phone: string; otp: string; deviceModel: string; os: string; browser: string }>()
);
