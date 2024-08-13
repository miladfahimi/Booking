import { NgModule } from '@angular/core';
import { RouterModule, Routes, PreloadAllModules } from '@angular/router';

const routes: Routes = [
  { path: 'auth', loadChildren: () => import('@tennis-time/auth').then(m => m.AuthModule) },
  { path: 'profile', loadChildren: () => import('@tennis-time/profile').then(m => m.ProfileModule) },
  { path: '', redirectTo: 'auth/signin', pathMatch: 'full' },  // Redirect root to signin
  { path: '**', redirectTo: 'auth/signin' }  // Fallback to signin on any undefined route
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
