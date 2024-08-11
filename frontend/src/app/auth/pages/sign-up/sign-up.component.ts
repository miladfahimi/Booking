import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthState } from '../../store/auth.reducer';
import { signUp } from '../../store/auth.actions';
import { selectAuthError, selectLoadingStatus, selectIsAuthenticated } from '../../store/auth.selectors';
import { LoadingStatus, SignUpReq } from '../../store/auth.models';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent implements OnInit {
  signUpForm: FormGroup;
  errorMessage$: Observable<string | null>;
  loadingStatus$: Observable<LoadingStatus>;
  isAuthenticated$: Observable<boolean>;

  constructor(
    private fb: FormBuilder,
    private store: Store<AuthState>,
    private router: Router
  ) {
    this.signUpForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]],
      phone: ['', Validators.required],
      role: ['USER']  // Default to 'USER'
    });

    this.errorMessage$ = this.store.pipe(select(selectAuthError));
    this.loadingStatus$ = this.store.pipe(select(selectLoadingStatus));
    this.isAuthenticated$ = this.store.pipe(select(selectIsAuthenticated));
  }

  ngOnInit(): void {
    // Navigate to sign-in page if sign-up is successful
    this.isAuthenticated$
      .pipe(
        tap((isAuthenticated: boolean) => {  // Explicitly define the type as boolean
          if (isAuthenticated) {
            this.router.navigate(['/auth/signin']);
          }
        })
      )
      .subscribe();
  }

  onSubmit() {
    if (this.signUpForm.valid) {
      const signUpData: SignUpReq = this.signUpForm.value;
      this.store.dispatch(signUp({ ...signUpData }));
    }
  }
}
