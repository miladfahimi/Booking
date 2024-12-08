import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { CoreAuthService } from '@tennis-time/core';  // Correct the import path

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: CoreAuthService, private router: Router) {}

  async canActivate(): Promise<boolean> {
    if (this.authService.isAuthenticated()) {
      return true;
    } else {
      this.router.navigate(['/auth/signin']);
      return false;
    }
  }
}
