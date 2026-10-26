package com.rentatools.billing.infrastructure.adapter.in.web;

import com.rentatools.billing.application.dto.PaymentHistoryView;
import com.rentatools.billing.application.port.in.GetClientPaymentHistoryQuery;
import com.rentatools.shared.domain.Actor;
import com.rentatools.shared.infrastructure.web.CurrentActor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST endpoint para consultar el historial de pagos del cliente autenticado.
 */
@RestController
@RequestMapping("/api/v1/pagos/cliente")
public class ClientPaymentHistoryController {

    private final GetClientPaymentHistoryQuery getClientPaymentHistoryQuery;

    public ClientPaymentHistoryController(GetClientPaymentHistoryQuery getClientPaymentHistoryQuery) {
        this.getClientPaymentHistoryQuery = getClientPaymentHistoryQuery;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<PaymentHistoryView> getClientPaymentHistory(
            @CurrentActor Actor actor,
            @PathVariable("id") UUID clientId
    ) {
        return getClientPaymentHistoryQuery.getClientPaymentHistory(actor, clientId);
    }
}
