package com.rentatools.billing.application.port.in;

import com.rentatools.billing.application.dto.PaymentHistoryView;
import com.rentatools.shared.domain.Actor;

import java.util.List;
import java.util.UUID;

/**
 * Consulta los movimientos de pago del cliente autenticado.
 */
public interface GetClientPaymentHistoryQuery {

    List<PaymentHistoryView> getClientPaymentHistory(Actor actor, UUID clientId);
}
