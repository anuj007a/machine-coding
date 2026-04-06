package com.wraith.domain.model;

import jakarta.persistence.*;
import lombok.*;
import com.wraith.domain.enums.PaymentStatus;
import com.wraith.domain.enums.PaymentType;

import java.time.Instant;

@Entity
@Table(name = "payments",
        uniqueConstraints = @UniqueConstraint(columnNames = "idempotencyKey"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String orderId;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String idempotencyKey;

    private Instant createdAt;
}