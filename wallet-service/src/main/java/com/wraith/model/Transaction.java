package com.wraith.model;

import jakarta.persistence.*;
import lombok.*;
import com.wraith.enums.TransactionStatus;
import com.wraith.enums.TransactionType;
import java.time.LocalDateTime;

/**
 * Transaction entity representing a single transaction in the wallet system.
 *
 * A transaction can be either a transfer between wallets or an add money transaction.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_from_wallet_id", columnList = "from_wallet_id"),
        @Index(name = "idx_to_wallet_id", columnList = "to_wallet_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transactionId;

    @Column(name = "from_wallet_id")
    private String fromWalletId;

    @Column(name = "to_wallet_id", nullable = false)
    private String toWalletId;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(length = 1000)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

