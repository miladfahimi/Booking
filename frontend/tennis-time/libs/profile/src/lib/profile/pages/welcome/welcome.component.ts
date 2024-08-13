import { Component, OnInit } from '@angular/core';
import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { ProfileState } from '../../store/profile.reducer';
import { UserProfileDTO } from '../../types';
import {selectProfile, selectProfileLoading} from '../../store/profile.selectors';
import * as ProfileActions from '../../store/profile.actions';
import { CoreAuthService } from '@tennis-time/core';
import {selectLoadingStatus} from "../../../../../../auth/src/lib/auth/store/auth.selectors"; // Import CoreAuthService

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {

  userData$: Observable<UserProfileDTO | null>;
  loadingStatus$: Observable<boolean>;

  constructor(
    private store: Store<ProfileState>,
    private coreAuthService: CoreAuthService  // Inject CoreAuthService
  ) {
    this.userData$ = this.store.pipe(select(selectProfile));
    this.loadingStatus$ = this.store.pipe(select(selectProfileLoading));
  }

  ngOnInit(): void {
    // Retrieve the userId from CoreAuthService
    const userId = this.coreAuthService.getUserId();
    this.userData$.subscribe({
      next: e => console.log(JSON.stringify(e)),
      error: err => console.error('Error:', err),
      complete: () => console.log('Subscription complete')
    });


    if (userId) {
      // Dispatch an action to load the profile data using the retrieved userId
      this.store.dispatch(ProfileActions.loadProfile({ userId }));
    } else {
      console.error('No userId found in local storage');
    }
  }
}
