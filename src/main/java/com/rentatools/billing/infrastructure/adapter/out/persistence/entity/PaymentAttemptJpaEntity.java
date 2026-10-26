package com.rentatools.billing.infrastructure.adapter.out.persistence.entity;

import com.rentatools.billing.domain.model.invoice.PaymentAttemptStatus;
import com.rentatools.billing.domain.model.invoice.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_attempts")
public class PaymentAttemptJpaEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "invoice_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID invoiceId;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false, length = 20)
    private PaymentMethod method;

    @Column(name = "reference", length = 500)
    private String reference;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "initiated_by", nullable = false, columnDefinition = "BINARY(16)")
    private UUID initiatedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private PaymentAttemptStatus status;

    public PaymentAttemptJpaEntity() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getInvoiceId() { return invoiceId; }
    public void setInvoiceId(UUID invoiceId) { this.invoiceId = invoiceId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
    public UUID getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(UUID initiatedBy) { this.initiatedBy = initiatedBy; }
    public PaymentAttemptStatus getStatus() { return status; }
    public void setStatus(PaymentAttemptStatus status) { this.status = status; }
}
