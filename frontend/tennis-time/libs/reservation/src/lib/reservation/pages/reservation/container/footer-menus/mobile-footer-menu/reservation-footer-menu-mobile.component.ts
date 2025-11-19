import { Component } from '@angular/core';

type FooterMenuIcon = 'home' | 'profile' | 'chat' | 'settings' | 'camera';

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
  readonly menuItems: FooterMenuItem[] = [
    { id: 'home', label: 'خانه', icon: 'home' },
    { id: 'profile', label: 'پروفایل', icon: 'profile' },
    { id: 'chat', label: 'پیام‌ها', icon: 'chat' },
    { id: 'camera', label: 'Photos', icon: 'camera' },
    { id: 'settings', label: 'تنظیمات', icon: 'settings' }
  ];

  activeItemId: string = this.menuItems[0]?.id ?? '';

  setActiveMenu(itemId: string): void {
    this.activeItemId = itemId;
  }

  trackByMenuId(_: number, item: FooterMenuItem): string {
    return item.id;
  }
}
