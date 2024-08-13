import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../services/profile.service';
import {LoadingStatus} from "@tennis-time/auth";
import {Observable, of} from "rxjs";
import {selectLoadingStatus} from "../../../../../../auth/src/lib/auth/store/auth.selectors";
import {select, Store} from "@ngrx/store";

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {

  userData: any;
  loadingStatus$: Observable<LoadingStatus>;


  constructor(private profileService: ProfileService, private store: Store) {

    this.loadingStatus$ = this.store.pipe(select(selectLoadingStatus));

  }

  ngOnInit(): void {
    this.profileService.initializeUser().subscribe(
      data => {
        this.userData = data;  // Store the user data
        console.log('User data:', this.userData);  // Log the user data for debugging
      },
      error => {
        console.error('Error fetching user data:', error);  // Log any errors
      }
    );
  }
}
