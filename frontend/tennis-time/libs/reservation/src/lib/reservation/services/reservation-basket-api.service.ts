import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import { ReservationStatus } from '../types';
import { ReservationBasketItem } from '../types/reservation-basket.types';

@Injectable({
  providedIn: 'root',
})
export class ReservationBasketApiService {
  private readonly host = window.location.hostname;
  private readonly baseUrl = `http://${this.host}:8083/api/v1/portal/user/basket`;

  constructor(private readonly http: HttpClient) { }

  getBasket(userId: string): Observable<ReservationBasketItem[]> {
    return this.http
      .get<ReservationBasketItem[]>(`${this.baseUrl}/${userId}`)
      .pipe(map(items => items.map(item => this.normalizeItem(item))));
  }

  addItem(item: ReservationBasketItem): Observable<ReservationBasketItem> {
    return this.http
      .post<ReservationBasketItem>(this.baseUrl, item)
      .pipe(map(saved => this.normalizeItem(saved)));
  }

  removeItem(userId: string, slotId: string): Observable<void> {
    const encodedSlotId = encodeURIComponent(slotId);
    return this.http.delete<void>(`${this.baseUrl}/${userId}/${encodedSlotId}`);
  }

  clear(userId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${userId}`);
  }

  private normalizeItem(item: ReservationBasketItem): ReservationBasketItem {
    const rawPrice = item.price as unknown;
    const price = typeof rawPrice === 'string'
      ? Number(rawPrice)
      : typeof rawPrice === 'number'
        ? rawPrice
        : 0;

    const rawDuration = item.durationMinutes as unknown;
    const durationMinutes = typeof rawDuration === 'string'
      ? Number(rawDuration)
      : typeof rawDuration === 'number'
        ? rawDuration
        : 0;

    return {
      ...item,
      price: Number.isFinite(price) ? price : 0,
      durationMinutes: Number.isFinite(durationMinutes) ? durationMinutes : 0,
      status: item.status ?? ReservationStatus.IN_BASKET,
    };
  }
}
