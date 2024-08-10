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
  responseData: any;

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
          this.responseData = response;
          this.router.navigate(['/profile']);  // Navigate to a different page after sign-in
        },
        error: (err) => {
          console.error('Sign in error', err);
          this.errorMessage = 'Invalid email or password. Please try again.';
        }
      });
    }
  }
}
