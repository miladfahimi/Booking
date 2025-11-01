import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WelcomeComponent } from './pages/welcome/welcome.component';
import { AuthGuard } from '@tennis-time/core';
import { InitializeComponent } from './pages/initialize/initialize.component';
import { ProfileInitGuard } from './guards/profile-init.guard';

const routes: Routes = [
  { path: 'welcome', component: WelcomeComponent, canActivate: [AuthGuard] },

  { path: 'entry', component: WelcomeComponent, canActivate: [AuthGuard, ProfileInitGuard] },

  { path: 'initialize', component: InitializeComponent, canActivate: [AuthGuard] },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfileRoutingModule { }
