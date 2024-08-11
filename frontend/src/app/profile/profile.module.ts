import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProfileRoutingModule } from './profile-routing.module';
import { WelcomeComponent } from './pages/welcome/welcome.component';
import { ProfileService } from './services/profile.service';  // Correct import for ProfileService

@NgModule({
  declarations: [
    WelcomeComponent
  ],
  imports: [
    CommonModule,
    ProfileRoutingModule
  ],
  providers: [
    ProfileService  // Ensure ProfileService is provided here if not provided in root
  ]
})
export class ProfileModule { }
