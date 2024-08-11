import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { AuthState } from '../../store/auth.reducer';
import { signIn } from '../../store/auth.actions';
import { selectAuthError, selectLoadingStatus } from '../../store/auth.selectors';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent implements OnInit {
  signInForm: FormGroup;
  errorMessage$: Observable<string | null>;
  loadingStatus$: Observable<{ loaded: boolean; loading: boolean }>;
  showPassword: boolean = false;

  constructor(
    private fb: FormBuilder,
    private store: Store<AuthState>
  ) {
    this.signInForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]]
    });

    this.errorMessage$ = this.store.pipe(select(selectAuthError));
    this.loadingStatus$ = this.store.pipe(select(selectLoadingStatus));
  }

  ngOnInit(): void {
    // Optionally handle any subscription logic here
  }

  onSubmit() {
    if (this.signInForm.valid) {
      const { email, password } = this.signInForm.value;
      this.store.dispatch(signIn({ email, password }));
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
