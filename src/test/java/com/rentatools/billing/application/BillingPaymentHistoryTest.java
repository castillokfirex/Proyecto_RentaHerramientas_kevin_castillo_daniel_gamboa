package com.rentatools.billing.application;

import com.rentatools.billing.application.BillingTestFakes.InMemoryInvoiceRepository;
import com.rentatools.billing.application.BillingTestFakes.InMemoryPaymentAttemptRepository;
import com.rentatools.billing.application.BillingTestFakes.InMemoryReservationParticipantsPort;
import com.rentatools.billing.application.BillingTestFakes.InMemorySecurityDepositRepository;
import com.rentatools.billing.application.dto.PaymentHistoryView;
import com.rentatools.billing.application.service.BillingQueryService;
import com.rentatools.billing.domain.model.deposit.SecurityDeposit;
import com.rentatools.billing.domain.model.invoice.Invoice;
import com.rentatools.billing.domain.model.invoice.InvoiceId;
import com.rentatools.billing.domain.model.invoice.InvoiceType;
import com.rentatools.billing.domain.model.invoice.Payment;
import com.rentatools.billing.domain.model.invoice.PaymentAttempt;
import com.rentatools.billing.domain.model.invoice.PaymentAttemptStatus;
import com.rentatools.billing.domain.model.invoice.PaymentKind;
import com.rentatools.billing.domain.model.invoice.PaymentMethod;
import com.rentatools.shared.application.exception.ForbiddenException;
import com.rentatools.shared.domain.Actor;
import com.rentatools.shared.domain.ActorRole;
import com.rentatools.shared.domain.Ids;
import com.rentatools.shared.domain.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BillingPaymentHistoryTest {

    @Test
    void returnsOnlyTheClientsPaymentsInReverseChronologicalOrder() {
        InMemoryInvoiceRepository invoices = new InMemoryInvoiceRepository();
        InMemoryPaymentAttemptRepository attempts = new InMemoryPaymentAttemptRepository();
        InMemorySecurityDepositRepository deposits = new InMemorySecurityDepositRepository();
        InMemoryReservationParticipantsPort participants = new InMemoryReservationParticipantsPort();
        UUID clientId = Ids.newId();
        UUID providerId = Ids.newId();
        UUID reservationId = Ids.newId();
        UUID otherReservationId = Ids.newId();
        Instant earlier = Instant.parse("2026-06-01T10:00:00Z");
        Instant later = earlier.plusSeconds(60);
        participants.register(reservationId, "RES-1", clientId, providerId);
        participants.register(otherReservationId, "RES-2", Ids.newId(), providerId);
        invoices.save(invoiceWithPayment(reservationId, earlier));
        invoices.save(invoiceWithPayment(reservationId, later));
        invoices.save(invoiceWithPayment(otherReservationId, later));
        SecurityDeposit deposit = SecurityDeposit.open(
                reservationId,
                Money.of(new BigDecimal("25.00")),
                earlier
        );
        deposit.collect(
                Money.of(new BigDecimal("25.00")),
                PaymentMethod.SIMULATED,
                null,
                later.plusSeconds(60),
                clientId
        );
        deposits.save(deposit);
        attempts.save(new PaymentAttempt(
                Ids.newId(),
                Ids.newId(),
                Money.of(new BigDecimal("12.00")),
                PaymentMethod.SIMULATED,
                "falló",
                later.plusSeconds(120),
                clientId,
                PaymentAttemptStatus.FAILED
        ));
        attempts.save(new PaymentAttempt(
                Ids.newId(),
                Ids.newId(),
                Money.of(new BigDecimal("14.00")),
                PaymentMethod.CARD,
                null,
                later.plusSeconds(180),
                clientId,
                PaymentAttemptStatus.PENDING
        ));

        BillingQueryService query = new BillingQueryService(invoices, attempts, deposits, participants);
        List<PaymentHistoryView> history = query.getClientPaymentHistory(
                new Actor(clientId, Set.of(ActorRole.CLIENTE)),
                clientId
        );

        assertThat(history).hasSize(5);
        assertThat(history).extracting(PaymentHistoryView::date)
                .containsExactly(later.plusSeconds(180), later.plusSeconds(120), later.plusSeconds(60), later, earlier);
        assertThat(history.subList(2, history.size())).allSatisfy(payment -> {
            assertThat(payment.amount()).isEqualByComparingTo("25.00");
            assertThat(payment.method()).isEqualTo(PaymentMethod.SIMULATED);
        });
        assertThat(history).extracting(PaymentHistoryView::status)
                .containsExactly("PENDIENTE", "FALLIDO", "PAGADO", "PAGADO", "PAGADO");
        assertThat(history.get(3).status()).isEqualTo("PAGADO");
    }

    @Test
    void deniesAccessWhenTheRequestedClientIsNotTheAuthenticatedClient() {
        BillingQueryService query = new BillingQueryService(
                new InMemoryInvoiceRepository(),
                new InMemoryPaymentAttemptRepository(),
                new InMemorySecurityDepositRepository(),
                new InMemoryReservationParticipantsPort()
        );
        UUID userId = Ids.newId();

        assertThatThrownBy(() -> query.getClientPaymentHistory(
                new Actor(userId, Set.of(ActorRole.CLIENTE)),
                Ids.newId()
        )).isInstanceOf(ForbiddenException.class);
    }

    private static Invoice invoiceWithPayment(UUID reservationId, Instant occurredAt) {
        Payment payment = new Payment(
                Ids.newId(),
                PaymentKind.PAYMENT,
                Money.of(new BigDecimal("25.00")),
                PaymentMethod.SIMULATED,
                null,
                occurredAt,
                Ids.newId()
        );
        return Invoice.reconstitute(
                InvoiceId.newId(),
                reservationId,
                InvoiceType.RENTAL,
                null,
                BigDecimal.ZERO,
                null,
                null,
                occurredAt,
                List.of(),
                List.of(payment),
                0L
        );
    }
}
