package com.rentatools.billing.domain.model.invoice;

import com.rentatools.shared.domain.Ids;
import com.rentatools.shared.domain.Money;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Registro durable del ciclo de un intento de pago.
 */
public record PaymentAttempt(
        UUID id,
        UUID invoiceId,
        Money amount,
        PaymentMethod method,
        String reference,
        Instant occurredAt,
        UUID initiatedBy,
        PaymentAttemptStatus status
) {

    public PaymentAttempt {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(invoiceId, "invoiceId must not be null");
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(method, "method must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
        Objects.requireNonNull(initiatedBy, "initiatedBy must not be null");
        Objects.requireNonNull(status, "status must not be null");
        if (reference != null) {
            reference = reference.trim();
            if (reference.isEmpty()) reference = null;
        }
    }

    public static PaymentAttempt pending(
            UUID invoiceId,
            Money amount,
            PaymentMethod method,
            String reference,
            Instant occurredAt,
            UUID initiatedBy
    ) {
        return new PaymentAttempt(
                Ids.newId(), invoiceId, amount, method, reference, occurredAt, initiatedBy, PaymentAttemptStatus.PENDING
        );
    }

    public PaymentAttempt withStatus(PaymentAttemptStatus newStatus) {
        return new PaymentAttempt(id, invoiceId, amount, method, reference, occurredAt, initiatedBy, newStatus);
    }
}
