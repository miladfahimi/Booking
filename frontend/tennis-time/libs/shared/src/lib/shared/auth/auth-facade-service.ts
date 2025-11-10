import { Injectable, NgZone } from '@angular/core';
import { CoreAuthService } from '@tennis-time/core';
import { BehaviorSubject, combineLatest, fromEvent, map, of, startWith, shareReplay } from 'rxjs';

export interface DecodedToken {
  userId?: string;
  sub?: string;
  roles?: string[];
  exp?: number;
  [k: string]: any;
}

@Injectable({ providedIn: 'root' })
export class AuthFacadeService {
  private decodedToken$ = new BehaviorSubject<DecodedToken | null>(null);

  readonly userEmail$ = this.decodedToken$.pipe(map(t => t?.sub ?? null), shareReplay(1));
  readonly userId$ = this.decodedToken$.pipe(map(t => t?.userId ?? null), shareReplay(1));
  readonly roles$ = this.decodedToken$.pipe(map(t => t?.roles ?? []), shareReplay(1));
  readonly isAuth$ = combineLatest([this.decodedToken$, of(null)]).pipe(
    map(([t]) => !!t && !this.isExpired(t)),
    shareReplay(1)
  );

  constructor(private core: CoreAuthService, private zone: NgZone) {
    this.loadFromStorage();

    this.zone.runOutsideAngular(() => {
      fromEvent<StorageEvent>(window, 'storage').pipe(startWith(null as any)).subscribe(evt => {
        this.zone.run(() => this.loadFromStorage());
      });
    });
  }

  private loadFromStorage(): void {
    const token = this.core.getToken();
    const decoded = token ? this.safeDecodeJwt(token) : null;
    this.decodedToken$.next(decoded);
  }

  hasRole(required: string | string[]) {
    const need = Array.isArray(required) ? required : [required];
    return this.roles$.pipe(map(rs => need.every(r => rs.indexOf(r) !== -1)));
  }

  isMyAccount(viewedId: string) {
    return this.userId$.pipe(map(myId => !!myId && myId === viewedId));
  }

  private isExpired(t: DecodedToken): boolean {
    if (!t?.exp) return false;
    const nowSec = Math.floor(Date.now() / 1000);
    return t.exp <= nowSec;
  }

  private safeDecodeJwt(token: string): DecodedToken | null {
    try {
      const [, payload] = token.split('.');
      const json = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
      return JSON.parse(decodeURIComponent(escape(json)));
    } catch {
      return null;
    }
  }

  refresh(): void {
    this.loadFromStorage();
  }
}
