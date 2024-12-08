import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { CoreAuthService } from '@tennis-time/core';

import { ProfileRoutingModule } from './profile-routing.module';
import { WelcomeComponent } from './pages/welcome/welcome.component';
import { ProfileService } from './services/profile.service';
import {SharedModule} from "@tennis-time/shared";
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { profileReducer } from './store/profile.reducer';
import { ProfileEffects } from './store/profile.effects';

@NgModule({
  declarations: [
    WelcomeComponent
  ],
  imports: [
    CommonModule,
    ProfileRoutingModule,
    NgOptimizedImage,
    SharedModule,
    StoreModule.forFeature('profile', profileReducer),
    EffectsModule.forFeature([ProfileEffects])
  ],
  providers: [
    ProfileService,  // Ensure ProfileService is provided here if not provided in root
    CoreAuthService,
  ]
})
export class ProfileModule { }
