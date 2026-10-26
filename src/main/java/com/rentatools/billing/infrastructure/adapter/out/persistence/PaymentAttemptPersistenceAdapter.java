package com.rentatools.billing.infrastructure.adapter.out.persistence;

import com.rentatools.billing.application.port.out.PaymentAttemptRepository;
import com.rentatools.billing.domain.model.invoice.PaymentAttempt;
import com.rentatools.billing.domain.model.invoice.PaymentAttemptStatus;
import com.rentatools.billing.domain.model.invoice.PaymentMethod;
import com.rentatools.billing.infrastructure.adapter.out.persistence.entity.PaymentAttemptJpaEntity;
import com.rentatools.billing.infrastructure.adapter.out.persistence.repository.SpringDataPaymentAttemptRepository;
import com.rentatools.shared.domain.Money;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class PaymentAttemptPersistenceAdapter implements PaymentAttemptRepository {

    private final SpringDataPaymentAttemptRepository repository;

    public PaymentAttemptPersistenceAdapter(SpringDataPaymentAttemptRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentAttempt save(PaymentAttempt attempt) {
        PaymentAttemptJpaEntity entity = repository.findById(attempt.id())
                .orElseGet(PaymentAttemptJpaEntity::new);
        entity.setId(attempt.id());
        entity.setInvoiceId(attempt.invoiceId());
        entity.setAmount(attempt.amount().amount());
        entity.setMethod(attempt.method());
        entity.setReference(attempt.reference());
        entity.setOccurredAt(attempt.occurredAt());
        entity.setInitiatedBy(attempt.initiatedBy());
        entity.setStatus(attempt.status());
        return toDomain(repository.saveAndFlush(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentAttempt> findAll() {
        return repository.findAll().stream().map(PaymentAttemptPersistenceAdapter::toDomain).toList();
    }

    private static PaymentAttempt toDomain(PaymentAttemptJpaEntity entity) {
        return new PaymentAttempt(
                entity.getId(),
                entity.getInvoiceId(),
                Money.of(entity.getAmount()),
                entity.getMethod(),
                entity.getReference(),
                entity.getOccurredAt(),
                entity.getInitiatedBy(),
                entity.getStatus()
        );
    }
}
