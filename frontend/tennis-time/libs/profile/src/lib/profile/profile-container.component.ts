import { ChangeDetectionStrategy, Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

@Component({
  selector: 'tt-profile-container',
  templateUrl: './profile-container.component.html',
  styleUrls: ['./profile-container.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProfileContainerComponent {
  basketBadgeCount$: Observable<number>;

  constructor(private readonly store: Store) {
    this.basketBadgeCount$ = this.store.select(state => {
      const reservationState = (state as Record<string, any>)?.['reservation'];
      const basket = Array.isArray(reservationState?.basket) ? reservationState.basket : [];

      return basket.length;
    });
  }
}