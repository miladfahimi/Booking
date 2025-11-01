import { Component, OnInit } from '@angular/core';
import { Store, select } from '@ngrx/store';
import { Observable, map, take } from 'rxjs';
import { ProfileState } from '../../store/profile.reducer';
import { UserProfileDTO } from '../../types';
import { selectProfile, selectProfileLoading } from '../../store/profile.selectors';
import * as ProfileActions from '../../store/profile.actions';
import { CoreAuthService } from '@tennis-time/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {

  userData$: Observable<UserProfileDTO | null>;
  loadingStatus$: Observable<boolean>;
  isIncomplete$: Observable<boolean>;

  constructor(
    private store: Store<ProfileState>,
    private coreAuthService: CoreAuthService,
    private router: Router
  ) {
    this.userData$ = this.store.pipe(select(selectProfile));
    this.loadingStatus$ = this.store.pipe(select(selectProfileLoading));
    this.isIncomplete$ = this.userData$.pipe(
      map(u => {
        if (!u) return true;
        const req = [u.firstName, u.lastName, u.phone, u.email];
        return req.some(v => !v || String(v).trim().length === 0);
      })
    );
  }

  ngOnInit(): void {
    const userId = this.coreAuthService.getUserId();
    if (userId) {
      this.store.dispatch(ProfileActions.loadProfile({ userId }));
      this.userData$.pipe(take(1)).subscribe(v => console.log('[Welcome] loaded profile once:', v));
    } else {
      console.error('No userId found in local storage');
    }
  }

  onLogout() {
    this.coreAuthService.signOut().subscribe({
      next: () => console.log('[WelcomeComponent] User signed out.'),
      error: err => console.error('[WelcomeComponent] Sign out error:', err)
    });
  }

  onEdit() {
    this.router.navigate(['/profile/initialize']);
  }
}
