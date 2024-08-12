import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../services/profile.service';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {

  userData: any;  // Property to store user data

  constructor(private profileService: ProfileService) { }

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
