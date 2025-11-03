import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { AuthState, LoadingStatus } from '@tennis-time/auth';
import { requestOtpSms, signInWithOtp } from '@tennis-time/auth';
import { selectAuthError, selectLoadingStatus } from '../../store/auth.selectors';
import * as UAParser from 'ua-parser-js';

@Component({
  selector: 'app-otp-sign-in',
  templateUrl: './otp-sign-in.component.html',
  styleUrls: ['./otp-sign-in.component.scss']
})
export class OtpSignInComponent implements OnInit {
  otpForm: FormGroup;
  errorMessage$: Observable<string | null>;
  loadingStatus$: Observable<LoadingStatus>;
  sent: boolean = false;
  counter: number = 0;
  timerRef: any;

  constructor(private fb: FormBuilder, private store: Store<AuthState>) {
    this.otpForm = this.fb.group({
      phone: ['', [Validators.required]],
      otp: ['']
    });

    this.errorMessage$ = this.store.pipe(select(selectAuthError));
    this.loadingStatus$ = this.store.pipe(select(selectLoadingStatus));
  }

  ngOnInit(): void {
  }

  sendCode() {
    const phoneCtrl = this.otpForm.get('phone');
    if (!phoneCtrl || phoneCtrl.invalid || this.counter > 0) {
      phoneCtrl?.markAsTouched();
      return;
    }

    this.store.dispatch(requestOtpSms({ phone: phoneCtrl.value }));
    this.sent = true;
    this.counter = 60;
    this.timerRef = setInterval(() => {
      this.counter--;
      if (this.counter <= 0 && this.timerRef) {
        clearInterval(this.timerRef);
      }
    }, 1000);
  }

  onSubmit() {
    console.log('OTP Form Values:', this.otpForm.value); // Debugging line
    if (this.otpForm.invalid) {
      this.otpForm.markAllAsTouched();
      return;
    }

    const { phone, otp } = this.otpForm.value;
    if (!otp) {
      return;
    }

    const parser = new UAParser();
    const uaResult = parser.getResult();
    const deviceModel = uaResult.device.model || 'unknown device';
    const os = uaResult.os.name || 'unknown OS';
    const browser = uaResult.browser.name || 'unknown browser';

    this.store.dispatch(signInWithOtp({ phone, otp, deviceModel, os, browser }));
  }
}
