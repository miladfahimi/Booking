import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { CoreAuthService } from '@tennis-time/core';

@Injectable({
  providedIn: 'root'
})
export class AlreadyAuthenticatedGuard implements CanActivate {
  constructor(private authService: CoreAuthService, private router: Router) {}

  canActivate(): boolean | UrlTree {
    if (this.authService.isAuthenticated()) {
      return this.router.parseUrl('/profile/welcome');
    }
    return true;
  }
}
