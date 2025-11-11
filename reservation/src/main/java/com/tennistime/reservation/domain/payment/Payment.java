package com.tennistime.reservation.domain.payment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate root that models a payment attempt for a reservation. The aggregate captures the
 * information exchanged with the bank gateway (token, reference numbers, status) while ensuring
 * we keep a consistent lifecycle for the payment flow.
 */
@Getter
@ToString
public class Payment {

    private final UUID id;
    private final UUID reservationId;
    private final BigDecimal amount;
    private final String referenceNumber;

    @Setter(AccessLevel.PACKAGE)
    private PaymentStatus status;
    private String token;
    private String bankState;
    private String bankStatusCode;
    private String bankReferenceNumber;
    private String retrievalReferenceNumber;
    private String traceNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime verifiedAt;
    private LocalDateTime reversedAt;

    private Payment(UUID id, UUID reservationId, BigDecimal amount, String referenceNumber) {
        this.id = id;
        this.reservationId = reservationId;
        this.amount = amount;
        this.referenceNumber = referenceNumber;
        this.status = PaymentStatus.CREATED;
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Factory method used to create a new payment aggregate for a reservation.
     *
     * @param reservationId reservation identifier that this payment belongs to.
     * @param amount        monetary amount for the payment.
     * @return initialized payment aggregate instance in {@link PaymentStatus#CREATED} state.
     */
    public static Payment create(UUID reservationId, BigDecimal amount) {
        Objects.requireNonNull(reservationId, "reservationId must not be null");
        Objects.requireNonNull(amount, "amount must not be null");
        return new Payment(UUID.randomUUID(), reservationId, amount, UUID.randomUUID().toString());
    }

    /**
     * Stores the issued payment token so the client can be redirected to the bank.
     *
     * @param token token issued by SEP upon requesting a payment session.
     */
    public void registerToken(String token) {
        Objects.requireNonNull(token, "token must not be null");
        if (token.isBlank()) {
            throw new IllegalArgumentException("token must not be blank");
        }
        this.token = token;
        this.status = PaymentStatus.TOKEN_ISSUED;
        touch();
    }

    /**
     * Records the callback data received from SEP so it can be processed idempotently.
     *
     * @param state      value of the {@code State} field returned by SEP.
     * @param statusCode value of the {@code Status} field returned by SEP.
     * @param refNum     bank reference number ({@code RefNum}).
     * @param rrn        retrieval reference number ({@code RRN}).
     * @param traceNo    bank trace number ({@code TraceNo}).
     */
    public void registerCallback(String state, String statusCode, String refNum, String rrn, String traceNo) {
        this.bankState = state;
        this.bankStatusCode = statusCode;
        this.bankReferenceNumber = refNum;
        this.retrievalReferenceNumber = rrn;
        this.traceNumber = traceNo;
        this.status = PaymentStatus.CALLBACK_RECEIVED;
        touch();
    }

    /**
     * Marks this payment as successfully verified with the acquiring bank.
     */
    public void markVerified() {
        this.status = PaymentStatus.VERIFIED;
        this.verifiedAt = LocalDateTime.now();
        touch();
    }

    /**
     * Flags the payment as failed. This is typically triggered when verification is not successful.
     */
    public void markFailed() {
        this.status = PaymentStatus.FAILED;
        touch();
    }

    /**
     * Marks the payment as reversed once a reverse transaction call succeeds.
     */
    public void markReversed() {
        this.status = PaymentStatus.REVERSED;
        this.reversedAt = LocalDateTime.now();
        touch();
    }

    /**
     * Indicates whether the payment has sufficient data to attempt a reverse call.
     *
     * @return {@code true} when the payment has been verified and has a reference number.
     */
    public boolean canBeReversed() {
        return PaymentStatus.VERIFIED.equals(this.status)
                && this.bankReferenceNumber != null
                && !this.bankReferenceNumber.isBlank();
    }

    /**
     * Updates the {@code updatedAt} timestamp to reflect a domain change.
     */
    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}
