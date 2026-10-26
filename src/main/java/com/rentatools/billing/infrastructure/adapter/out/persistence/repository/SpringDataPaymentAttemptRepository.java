package com.rentatools.billing.infrastructure.adapter.out.persistence.repository;

import com.rentatools.billing.infrastructure.adapter.out.persistence.entity.PaymentAttemptJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataPaymentAttemptRepository extends JpaRepository<PaymentAttemptJpaEntity, UUID> {
}
