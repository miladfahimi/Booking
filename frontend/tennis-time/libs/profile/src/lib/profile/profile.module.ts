import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { CoreAuthService } from '@tennis-time/core';

import { ProfileRoutingModule } from './profile-routing.module';
import { WelcomeComponent } from './pages/welcome/welcome.component';
import { ProfileService } from './services/profile.service';  // Correct import for ProfileService

@NgModule({
  declarations: [
    WelcomeComponent
  ],
  imports: [
    CommonModule,
    ProfileRoutingModule,
    NgOptimizedImage
  ],
  providers: [
    ProfileService,  // Ensure ProfileService is provided here if not provided in root
    CoreAuthService,
  ]
})
export class ProfileModule { }
