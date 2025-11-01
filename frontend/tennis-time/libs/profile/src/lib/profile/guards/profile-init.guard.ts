import { Injectable, inject } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { ProfileService } from '../services/profile.service';
import { CoreAuthService } from '@tennis-time/core';
import { catchError, map, of } from 'rxjs';
import { UserInitializationResponseDTO } from '../types';

@Injectable({ providedIn: 'root' })
export class ProfileInitGuard implements CanActivate {
  private api = inject(ProfileService);
  private auth = inject(CoreAuthService);
  private router = inject(Router);

  canActivate() {
    const userId = this.auth.getUserId();
    if (!userId) {
      this.router.navigate(['/auth/signin']);
      return of(false);
    }

    return this.api.getInitialization(userId).pipe(
      map((res: UserInitializationResponseDTO) => {
        if (res?.userProfilesInitiated) {
          this.router.navigate(['/reservation/book']);
          return false;
        }
        return true;
      }),
      catchError(() => of(true))
    );
  }
}
