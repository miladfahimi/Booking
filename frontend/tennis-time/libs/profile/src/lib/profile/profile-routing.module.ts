import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WelcomeComponent } from './pages/welcome/welcome.component';
import { AuthGuard } from '@tennis-time/core';
import { InitializeComponent } from './pages/initialize/initialize.component';
import { ProfileInitGuard } from './guards/profile-init.guard';
import { ProfileContainerComponent } from './profile-container.component';

const routes: Routes = [
  {
    path: '',
    component: ProfileContainerComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'welcome', component: WelcomeComponent },
      { path: 'entry', component: WelcomeComponent, canActivate: [ProfileInitGuard] },
      { path: 'initialize', component: InitializeComponent },
      { path: '', pathMatch: 'full', redirectTo: 'welcome' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfileRoutingModule { }