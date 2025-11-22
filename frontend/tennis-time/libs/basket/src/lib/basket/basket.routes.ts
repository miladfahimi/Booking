import { Route } from '@angular/router';
import { AuthGuard } from '@tennis-time/core';
import { BasketComponent } from './basket.component';

export const basketRoutes: Route[] = [
  {
    path: '',
    component: BasketComponent,
    canActivate: [AuthGuard],
  },
];
