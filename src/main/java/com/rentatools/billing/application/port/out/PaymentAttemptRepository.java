package com.rentatools.billing.application.port.out;

import com.rentatools.billing.domain.model.invoice.PaymentAttempt;

import java.util.List;

/**
 * Persistencia de los intentos de pago y sus estados.
 */
public interface PaymentAttemptRepository {

    PaymentAttempt save(PaymentAttempt paymentAttempt);

    List<PaymentAttempt> findAll();
}
