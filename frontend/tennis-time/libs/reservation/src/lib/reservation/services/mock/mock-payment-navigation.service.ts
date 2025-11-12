import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class MockPaymentNavigationService {
  constructor(private readonly router: Router) { }

  open(url: string): void {
    if (typeof window === 'undefined' || !url) {
      return;
    }

    const target = this.normalize(url);
    const newWindow = window.open(target, '_blank');

    if (newWindow) {
      try {
        newWindow.opener = null;
      } catch {
        // ignore if browser prevents assigning opener
      }
      return;
    }

    if (target.startsWith(window.location.origin)) {
      const relative = target.slice(window.location.origin.length) || '/';
      this.router.navigateByUrl(relative);
    } else {
      window.location.href = target;
    }
  }

  private normalize(url: string): string {
    if (!url.startsWith('http')) {
      const path = url.startsWith('/') ? url : `/${url}`;
      return `${window.location.origin}${path}`;
    }

    return url;
  }
}