export interface PaymentInitiationPayload {
  reservationId: string;
  amount: number;
  customerCellNumber?: string | null;
  tokenExpiryInMinutes?: number | null;
  hashedCardNumber?: string | null;
  useGetMethod?: boolean | null;
}

export interface PaymentInitiationResult {
  paymentId: string;
  token?: string | null;
  redirectUrl?: string | null;
  referenceNumber?: string | null;
}

export interface PaymentSessionReservation {
  id: string;
  reservationDate: string;
  startTime: string;
  endTime: string;
}

export interface PaymentMockSessionData {
  paymentId: string;
  referenceNumber: string | null;
  redirectUrl: string;
  providerRedirectUrl: string | null;
  total: number;
  reservations: PaymentSessionReservation[];
  reservationDate: string;
}