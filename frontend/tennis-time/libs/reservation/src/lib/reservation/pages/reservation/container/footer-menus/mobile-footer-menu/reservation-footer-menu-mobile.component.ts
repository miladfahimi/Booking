import { Component, EventEmitter, Input, Output } from '@angular/core';

type FooterMenuIcon = 'home' | 'profile' | 'chat' | 'settings' | 'basket';

interface FooterMenuItem {
  id: string;
  label: string;
  icon: FooterMenuIcon;
}

@Component({
  selector: 'app-reservation-footer-menu-mobile',
  templateUrl: './reservation-footer-menu-mobile.component.html',
  styleUrls: ['./reservation-footer-menu-mobile.component.scss']
})
export class ReservationFooterMenuMobileComponent {
  @Input() basketCount = 0;
  @Input() basketActive = false;
  @Output() basketSelected = new EventEmitter<void>();

  readonly menuItems: FooterMenuItem[] = [
    { id: 'home', label: 'خانه', icon: 'home' },
    { id: 'basket', label: 'سبد رزرو', icon: 'basket' },
    { id: 'chat', label: 'پیام‌ها', icon: 'chat' },
    { id: 'profile', label: 'پروفایل', icon: 'profile' },
    { id: 'settings', label: 'تنظیمات', icon: 'settings' }
  ];

  activeItemId: string = this.menuItems[0]?.id ?? '';

  setActiveMenu(itemId: string): void {
    if (itemId === 'basket') {
      this.basketSelected.emit();
      return;
    }
    this.activeItemId = itemId;
  }

  isItemActive(itemId: string): boolean {
    if (itemId === 'basket') {
      return this.basketActive;
    }
    return itemId === this.activeItemId;
  }

  trackByMenuId(_: number, item: FooterMenuItem): string {
    return item.id;
  }
}