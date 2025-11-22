import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';

export interface FooterMenuItem {
  id: string;
  label: string;
  icon: string;
}

const DEFAULT_MENU_ITEMS: FooterMenuItem[] = [
  { id: 'basket', label: 'سبد رزرو', icon: 'Buy.png' },
  { id: 'home', label: 'خانه', icon: 'Home.png' },
  { id: 'calendar', label: 'رزرو', icon: 'Calendar.png' },
  { id: 'profile', label: 'پروفایل', icon: 'Profile.png' },
  { id: 'chat', label: 'پیام‌ها', icon: 'Notification.png' },
];

@Component({
  selector: 'app-footer-menu',
  templateUrl: './footer-menu.component.html',
  styleUrls: ['./footer-menu.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FooterMenuComponent implements OnChanges {
  @Input() menuItems: FooterMenuItem[] = DEFAULT_MENU_ITEMS;
  @Input() activeItemId?: string;
  @Input() activeItemIds: string[] = [];
  @Input() badgeByItemId: Record<string, number> = {};
  @Output() menuSelected = new EventEmitter<string>();

  private internalActiveId = this.menuItems[0]?.id ?? '';

  constructor(private router: Router) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['menuItems']) {
      this.internalActiveId = this.menuItems[0]?.id ?? '';
    }
  }

  get resolvedActiveId(): string {
    return this.activeItemId ?? this.internalActiveId;
  }

  setActiveMenu(itemId: string): void {
    this.menuSelected.emit(itemId);

    if (itemId === 'profile') {
      this.router.navigate(['/welcome/profile']);
    }

    if (itemId === 'calendar') {
      this.router.navigate(['/reservation/book']);
    }

    if (this.activeItemId) {
      return;
    }

    if (this.activeItemIds.indexOf(itemId) === -1) {
      this.internalActiveId = itemId;
    }
  }

  isItemActive(itemId: string): boolean {
    if (this.activeItemIds.indexOf(itemId) !== -1) {
      return true;
    }

    return itemId === this.resolvedActiveId;
  }

  badgeCount(itemId: string): number | undefined {
    const badge = this.badgeByItemId[itemId];
    return badge && badge > 0 ? badge : undefined;
  }

  iconPath(icon: string): string {
    return icon.startsWith('/') ? icon : `/icons/${icon}`;
  }

  trackByMenuId(_: number, item: FooterMenuItem): string {
    return item.id;
  }
}
