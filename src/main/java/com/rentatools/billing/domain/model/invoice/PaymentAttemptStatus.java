package com.rentatools.billing.domain.model.invoice;

/**
 * Estado de un intento de pago, independiente del movimiento contable confirmado.
 */
public enum PaymentAttemptStatus {
    PENDING,
    PAID,
    FAILED;

    public String displayName() {
        return switch (this) {
            case PENDING -> "PENDIENTE";
            case PAID -> "PAGADO";
            case FAILED -> "FALLIDO";
        };
    }
}
