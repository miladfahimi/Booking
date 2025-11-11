package com.tennistime.reservation.infrastructure.persistence.payment;

import com.tennistime.reservation.domain.payment.Payment;
import com.tennistime.reservation.domain.payment.PaymentRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of {@link PaymentRepository}. This implementation is meant for the
 * first iteration of the payment module where no persistent storage has been provisioned yet.
 */
@Repository
public class InMemoryPaymentRepository implements PaymentRepository {

    private final Map<UUID, Payment> payments = new ConcurrentHashMap<>();
    private final Map<String, UUID> paymentsByReference = new ConcurrentHashMap<>();

    @Override
    public Payment save(Payment payment) {
        payments.put(payment.getId(), payment);
        paymentsByReference.put(payment.getReferenceNumber(), payment.getId());
        return payment;
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return Optional.ofNullable(payments.get(id));
    }

    @Override
    public Optional<Payment> findByReferenceNumber(String referenceNumber) {
        UUID paymentId = paymentsByReference.get(referenceNumber);
        return paymentId != null ? Optional.ofNullable(payments.get(paymentId)) : Optional.empty();
    }

    @Override
    public boolean existsByReferenceNumber(String referenceNumber) {
        return paymentsByReference.containsKey(referenceNumber);
    }
}
