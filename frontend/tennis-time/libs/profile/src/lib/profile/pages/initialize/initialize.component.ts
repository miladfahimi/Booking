import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, Validators, FormControl, FormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { Router } from '@angular/router';
import { initializeProfile } from '../../store/profile.actions';
import { selectProfileError, selectProfileLoading } from '../../store/profile.selectors';
import { UserInitializationRequestDTO } from '../../types';
import { CoreAuthService } from '@tennis-time/core';

type InitializeFormControls = {
  userId: FormControl<string>;
  email: FormControl<string | null>;
  firstName: FormControl<string>;
  lastName: FormControl<string>;
  phone: FormControl<string>;
  address: FormControl<string | null>;
  gender: FormControl<string | null>;
  dateOfBirth: FormControl<string | null>;
};

@Component({
  selector: 'tt-profile-initialize',
  templateUrl: './initialize.component.html',
  styleUrls: ['./initialize.component.scss']
})
export class InitializeComponent implements OnInit {
  private fb = inject(FormBuilder);
  private store = inject(Store);
  private auth = inject(CoreAuthService);
  private router = inject(Router);

  loading$ = this.store.select(selectProfileLoading);
  error$ = this.store.select(selectProfileError);

  form: FormGroup<InitializeFormControls> = this.fb.group<InitializeFormControls>({
    userId: this.fb.control<string>('', { validators: [Validators.required], nonNullable: true }),
    email: this.fb.control<string | null>(null, { validators: [Validators.email] }),
    firstName: this.fb.control<string>('', { validators: [Validators.required], nonNullable: true }),
    lastName: this.fb.control<string>('', { validators: [Validators.required], nonNullable: true }),
    phone: this.fb.control<string>('', { validators: [Validators.required], nonNullable: true }),
    address: this.fb.control<string | null>(null),
    gender: this.fb.control<string | null>(null),
    dateOfBirth: this.fb.control<string | null>(null)
  });

  ngOnInit(): void {
    const userId = this.auth.getUserId();
    if (userId) this.form.controls.userId.setValue(userId);
  }

  t<K extends keyof InitializeFormControls>(key: K) {
    return this.form.controls[key];
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    const payload: UserInitializationRequestDTO = {
      userProfileDTO: {
        userId: v.userId,
        email: v.email ?? undefined,
        firstName: v.firstName,
        lastName: v.lastName,
        phone: v.phone,
        address: v.address ?? undefined,
        gender: v.gender ?? undefined,
        dateOfBirth: v.dateOfBirth ?? undefined
      }
    };
    this.store.dispatch(initializeProfile({ payload }));
  }

  cancel() {
    this.router.navigate(['/profile/welcome']);
  }
}
