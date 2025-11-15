import { Injectable, NgZone, OnDestroy } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { ReservationStatus } from '../types';
import { SlotStatusNotification } from './slot-status-notification';

@Injectable({ providedIn: 'root' })
export class SlotStatusRealtimeService implements OnDestroy {
  private socket: WebSocket | null = null;
  private reconnectHandle: number | null = null;
  private readonly subscriptionId = 'slot-status';
  private readonly updatesSubject = new Subject<SlotStatusNotification>();

  constructor(private zone: NgZone) {}

  get updates$(): Observable<SlotStatusNotification> {
    return this.updatesSubject.asObservable();
  }

  connect(): void {
    if (this.socket || typeof window === 'undefined') {
      return;
    }
    if (this.reconnectHandle !== null) {
      window.clearTimeout(this.reconnectHandle);
      this.reconnectHandle = null;
    }
    this.zone.runOutsideAngular(() => this.openSocket());
  }

  disconnect(): void {
    if (typeof window !== 'undefined' && this.reconnectHandle !== null) {
      window.clearTimeout(this.reconnectHandle);
      this.reconnectHandle = null;
    }
    if (!this.socket) {
      return;
    }
    const current = this.cleanupSocket();
    current?.close();
  }

  ngOnDestroy(): void {
    this.disconnect();
  }

  private openSocket(): void {
    const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws';
    const host = window.location.hostname;
    const url = `${protocol}://${host}:8083/api/v1/ws/bff`;
    try {
      const socket = new WebSocket(url, ['v12.stomp', 'v11.stomp', 'v10.stomp']);
      socket.onopen = () => this.handleOpen();
      socket.onmessage = event => this.handleMessage(event);
      socket.onclose = () => this.handleClose();
      socket.onerror = () => this.handleClose();
      this.socket = socket;
    } catch {
      this.scheduleReconnect();
    }
  }

  private handleOpen(): void {
    this.sendFrame(['CONNECT', 'accept-version:1.2', `host:${window.location.hostname}`], '');
  }

  private handleMessage(event: MessageEvent): void {
    const payload = typeof event.data === 'string' ? event.data : '';
    if (!payload) {
      return;
    }
    const frames = payload.split('\u0000').filter(frame => frame);
    for (const frame of frames) {
      this.processFrame(frame);
    }
  }

  private processFrame(frame: string): void {
    const lines = frame.replace(/\r/g, '').split('\n');
    const command = lines.shift()?.trim();
    if (!command) {
      return;
    }
    const separatorIndex = lines.indexOf('');
    const bodyStartIndex = separatorIndex === -1 ? lines.length : separatorIndex + 1;
    const bodyLines = lines.slice(bodyStartIndex);
    if (command === 'CONNECTED') {
      this.sendFrame(['SUBSCRIBE', `id:${this.subscriptionId}`, 'destination:/topic/slot-status', 'ack:auto'], '');
      return;
    }
    if (command === 'MESSAGE') {
      const body = bodyLines.join('\n');
      if (!body) {
        return;
      }
      this.emitUpdate(body);
      return;
    }
    if (command === 'ERROR') {
      this.handleClose();
    }
  }

  private emitUpdate(body: string): void {
    try {
      const parsed = JSON.parse(body) as SlotStatusNotification;
      const status = parsed.status as ReservationStatus;
      this.zone.run(() => this.updatesSubject.next({ ...parsed, status }));
    } catch {}
  }

  private handleClose(): void {
    const current = this.cleanupSocket();
    if (current && current.readyState === WebSocket.OPEN) {
      current.close();
    }
    this.scheduleReconnect();
  }

  private cleanupSocket(): WebSocket | null {
    if (!this.socket) {
      return null;
    }
    const current = this.socket;
    current.onopen = null;
    current.onmessage = null;
    current.onclose = null;
    current.onerror = null;
    this.socket = null;
    return current;
  }

  private sendFrame(segments: string[], body: string): void {
    if (!this.socket || this.socket.readyState !== WebSocket.OPEN) {
      return;
    }
    const frame = `${segments.join('\n')}\n\n${body}\u0000`;
    this.socket.send(frame);
  }

  private scheduleReconnect(): void {
    if (typeof window === 'undefined') {
      return;
    }
    if (this.reconnectHandle !== null) {
      return;
    }
    this.reconnectHandle = window.setTimeout(() => {
      this.reconnectHandle = null;
      if (!this.socket) {
        this.openSocket();
      }
    }, 5000);
  }
}
