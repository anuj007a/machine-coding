CREATE TABLE orders (
                        id VARCHAR PRIMARY KEY,
                        amount BIGINT NOT NULL,
                        currency VARCHAR NOT NULL,
                        status VARCHAR NOT NULL,
                        created_at TIMESTAMP NOT NULL
);

CREATE TABLE payments (
                          id VARCHAR PRIMARY KEY,
                          order_id VARCHAR NOT NULL,
                          type VARCHAR NOT NULL,
                          amount BIGINT NOT NULL,
                          status VARCHAR NOT NULL,
                          idempotency_key VARCHAR UNIQUE,
                          created_at TIMESTAMP NOT NULL
);

CREATE TABLE idempotency (
                             key VARCHAR PRIMARY KEY,
                             request_hash VARCHAR,
                             response TEXT,
                             created_at TIMESTAMP
);

CREATE INDEX idx_payment_order ON payments(order_id);