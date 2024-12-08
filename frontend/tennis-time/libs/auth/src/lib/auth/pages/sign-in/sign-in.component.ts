import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { AuthState, LoadingStatus } from '@tennis-time/auth';
import { signIn } from '@tennis-time/auth';
import { selectAuthError, selectLoadingStatus } from '../../store/auth.selectors';
import * as UAParser from 'ua-parser-js';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent implements OnInit {
  signInForm: FormGroup;
  errorMessage$: Observable<string | null>;
  loadingStatus$: Observable<LoadingStatus>;
  showPassword: boolean = false;

  constructor(private fb: FormBuilder, private store: Store<AuthState>) {
    this.signInForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]]
    });

    this.errorMessage$ = this.store.pipe(select(selectAuthError));
    this.loadingStatus$ = this.store.pipe(select(selectLoadingStatus));
  }

  ngOnInit(): void {
  }

  onSubmit() {
    if (this.signInForm.valid) {
      const { email, password } = this.signInForm.value;

      const parser = new UAParser();
      const uaResult = parser.getResult();
      const deviceModel = uaResult.device.model || 'unknown device';
      const os = uaResult.os.name || 'unknown OS';
      const browser = uaResult.browser.name || 'unknown browser';

      this.store.dispatch(signIn({ email, password, deviceModel, os, browser }));
    }
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
    const passwordField = this.signInForm.get('password');
    if (passwordField) {
      (document.querySelector('[formControlName="password"]') as HTMLInputElement).type = this.showPassword ? 'text' : 'password';
    }
  }
}
