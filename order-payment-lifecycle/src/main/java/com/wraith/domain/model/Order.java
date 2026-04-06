package com.wraith.domain.model;

import jakarta.persistence.*;
import lombok.*;
import com.wraith.domain.enums.OrderStatus;

import java.time.Instant;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Long amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Instant createdAt;
}