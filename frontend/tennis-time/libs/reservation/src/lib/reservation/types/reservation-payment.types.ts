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
