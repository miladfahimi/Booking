import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent {
  signUpForm: FormGroup;
  errorMessage: string = '';
  responseData: any;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.signUpForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]],
      phone: ['', Validators.required],
      role: ['USER', Validators.required] // Default to 'USER' or let the user choose
    });
  }

  onSubmit() {
    if (this.signUpForm.valid) {
      const { username, email, password, phone, role } = this.signUpForm.value;
      this.authService.signUp(username, email, password, phone, role).subscribe({
        next: (response) => {
          console.log('Sign up successful', response);
          this.responseData = response;
          this.router.navigate(['/auth/signin']);
        },
        error: (err) => {
          if (err.status === 409) {
            this.errorMessage = 'Email is already registered. Please use a different email.';
          } else {
            console.error('Sign up error', err);
            this.errorMessage = 'An error occurred during sign-up. Please try again.';
          }
        }
      });
    }
  }
}
