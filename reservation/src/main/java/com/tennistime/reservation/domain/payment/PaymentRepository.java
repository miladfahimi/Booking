package com.tennistime.reservation.domain.payment;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository abstraction for persisting {@link Payment} aggregates.
 */
public interface PaymentRepository {

    /**
     * Persists the provided payment aggregate.
     *
     * @param payment aggregate to persist.
     * @return persisted payment instance.
     */
    Payment save(Payment payment);

    /**
     * Locates a payment using its identifier.
     *
     * @param id unique payment identifier.
     * @return optional containing the payment when it exists.
     */
    Optional<Payment> findById(UUID id);

    /**
     * Finds a payment using the {@code ResNum} value that was sent to the bank.
     *
     * @param referenceNumber local reference number shared with SEP.
     * @return optional containing the payment aggregate when it exists.
     */
    Optional<Payment> findByReferenceNumber(String referenceNumber);

    /**
     * Indicates whether the provided {@code ResNum} already exists.
     *
     * @param referenceNumber {@code ResNum} reference.
     * @return {@code true} when a payment already uses the reference number.
     */
    boolean existsByReferenceNumber(String referenceNumber);
}
