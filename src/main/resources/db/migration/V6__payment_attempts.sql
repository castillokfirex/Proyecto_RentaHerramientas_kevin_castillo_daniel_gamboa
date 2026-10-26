CREATE TABLE payment_attempts (
    id           BINARY(16)    NOT NULL,
    invoice_id   BINARY(16)    NOT NULL,
    amount       DECIMAL(12,2) NOT NULL,
    method       VARCHAR(20)   NOT NULL,
    reference    VARCHAR(500)  NULL,
    occurred_at  DATETIME(6)   NOT NULL,
    initiated_by BINARY(16)    NOT NULL,
    status       VARCHAR(10)   NOT NULL,
    CONSTRAINT pk_payment_attempts PRIMARY KEY (id),
    CONSTRAINT fk_payment_attempts_invoice FOREIGN KEY (invoice_id) REFERENCES invoices (id),
    CONSTRAINT fk_payment_attempts_actor FOREIGN KEY (initiated_by) REFERENCES users (id),
    CONSTRAINT ck_payment_attempts_amount CHECK (amount > 0),
    CONSTRAINT ck_payment_attempts_method CHECK (method IN ('CASH', 'CARD', 'TRANSFER', 'SIMULATED')),
    CONSTRAINT ck_payment_attempts_status CHECK (status IN ('PENDING', 'PAID', 'FAILED'))
);

CREATE INDEX ix_payment_attempts_actor_date ON payment_attempts (initiated_by, occurred_at);
