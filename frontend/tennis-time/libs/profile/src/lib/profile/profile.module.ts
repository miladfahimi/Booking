import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { CoreAuthService } from '@tennis-time/core';

import { ProfileRoutingModule } from './profile-routing.module';
import { WelcomeComponent } from './pages/welcome/welcome.component';
import { InitializeComponent } from './pages/initialize/initialize.component';
import { ProfileService } from './services/profile.service';
import { SharedModule } from '@tennis-time/shared';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { profileReducer } from './store/profile.reducer';
import { ProfileEffects } from './store/profile.effects';

@NgModule({
  declarations: [
    WelcomeComponent,
    InitializeComponent
  ],
  imports: [
    CommonModule,
    ProfileRoutingModule,
    NgOptimizedImage,
    SharedModule,
    ReactiveFormsModule,
    StoreModule.forFeature('profile', profileReducer),
    EffectsModule.forFeature([ProfileEffects])
  ],
  providers: [
    ProfileService,
    CoreAuthService,
  ]
})
export class ProfileModule { }
