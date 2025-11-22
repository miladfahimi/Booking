import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { finalize } from 'rxjs';
import { CoreAuthService } from '@tennis-time/core';
import { SharedModule } from '@tennis-time/shared';
import { ReservationBasketApiService } from '@tennis-time/reservation';
import { ReservationBasketItem } from '@tennis-time/reservation';

@Component({
  selector: 'lib-basket',
  standalone: true,
  imports: [CommonModule, RouterModule, SharedModule],
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.scss'],
})
export class BasketComponent implements OnInit {
  items: ReservationBasketItem[] = [];
  loading = false;
  errorMessage: string | null = null;
  private userId: string | null = null;

  constructor(
    private readonly basketApi: ReservationBasketApiService,
    private readonly authService: CoreAuthService,
    private readonly router: Router,
  ) { }

  ngOnInit(): void {
    this.userId = this.authService.getUserId();

    if (!this.userId) {
      this.errorMessage = 'برای مشاهده سبد ابتدا باید وارد شوید.';
      return;
    }

    this.loadBasket();
  }

  get total(): number {
    return this.items.reduce((sum, item) => sum + (item.price ?? 0), 0);
  }

  get badgeCount(): number {
    return this.items.length;
  }

  refresh(): void {
    if (!this.userId) {
      return;
    }

    this.loadBasket();
  }

  onRemove(slotId: string): void {
    if (!this.userId) {
      return;
    }

    this.loading = true;
    this.basketApi.removeItem(this.userId, slotId)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: () => {
          this.items = this.items.filter(item => item.slotId !== slotId);
        },
        error: () => {
          this.errorMessage = 'حذف آیتم با مشکل مواجه شد. لطفاً دوباره تلاش کنید.';
        }
      });
  }

  goToBooking(): void {
    this.router.navigate(['/reservation/book']);
  }

  onFooterMenuSelected(itemId: string): void {
    if (itemId === 'basket') {
      this.goToBooking();
      return;
    }

    if (itemId === 'home') {
      this.router.navigate(['/auth/signin']);
    }
  }

  trackBySlotId(_: number, item: ReservationBasketItem): string {
    return item.slotId;
  }

  private loadBasket(): void {
    if (!this.userId) {
      return;
    }

    this.loading = true;
    this.errorMessage = null;

    this.basketApi.getBasket(this.userId)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: items => {
          this.items = items ?? [];
        },
        error: () => {
          this.errorMessage = 'دریافت اطلاعات سبد با مشکل مواجه شد.';
        }
      });
  }
}