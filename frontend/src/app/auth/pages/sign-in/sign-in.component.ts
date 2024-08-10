import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent {
  signInForm: FormGroup;
  errorMessage: string = '';
  showPassword: boolean = false;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.signInForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  onSubmit() {
    if (this.signInForm.valid) {
      const { email, password } = this.signInForm.value;
      this.authService.signIn(email, password).subscribe({
        next: (response) => {
          console.log('Sign in successful', response);
          this.router.navigate(['/home']);  // Redirect to home or dashboard
        },
        error: (err) => {
          if (err.status === 403) {
            this.errorMessage = 'Invalid email or password. Please try again.';
          } else {
            console.error('Sign in error', err);
            this.errorMessage = 'An error occurred during sign-in. Please try again.';
          }
        }
      });
    }
  }

togglePasswordVisibility(): void {
  this.showPassword = !this.showPassword;
  const passwordField = this.signInForm.get('password');
  if (passwordField) {
    (document.querySelector('[formControlName="password"]') as HTMLInputElement).type = this.showPassword ? 'text' : 'password';
  }
}}
