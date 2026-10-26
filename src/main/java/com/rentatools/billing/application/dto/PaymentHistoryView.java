package com.rentatools.billing.application.dto;

import com.rentatools.billing.domain.model.deposit.DepositTransaction;
import com.rentatools.billing.domain.model.deposit.DepositTransactionKind;
import com.rentatools.billing.domain.model.invoice.Payment;
import com.rentatools.billing.domain.model.invoice.PaymentKind;
import com.rentatools.billing.domain.model.invoice.PaymentMethod;
import com.rentatools.billing.domain.model.invoice.PaymentAttempt;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Movimiento individual presentado en el historial de pagos del cliente.
 */
public record PaymentHistoryView(
        UUID id,
        Instant date,
        BigDecimal amount,
        PaymentMethod method,
        String status
) {

    public static PaymentHistoryView from(Payment payment) {
        Objects.requireNonNull(payment, "payment must not be null");
        String status = payment.kind() == PaymentKind.PAYMENT ? "PAGADO" : "REEMBOLSADO";
        return new PaymentHistoryView(
                payment.id(),
                payment.occurredAt(),
                payment.amount().amount(),
                payment.method(),
                status
        );
    }

    public static PaymentHistoryView from(DepositTransaction transaction) {
        Objects.requireNonNull(transaction, "transaction must not be null");
        String status = transaction.kind() == DepositTransactionKind.COLLECTION ? "PAGADO" : "REEMBOLSADO";
        return new PaymentHistoryView(
                transaction.id(),
                transaction.occurredAt(),
                transaction.amount().amount(),
                transaction.method(),
                status
        );
    }

    public static PaymentHistoryView from(PaymentAttempt attempt) {
        Objects.requireNonNull(attempt, "attempt must not be null");
        return new PaymentHistoryView(
                attempt.id(),
                attempt.occurredAt(),
                attempt.amount().amount(),
                attempt.method(),
                attempt.status().displayName()
        );
    }
}
