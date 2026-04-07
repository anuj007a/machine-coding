package com.wraith.model;

import jakarta.persistence.*;
import lombok.*;
import com.wraith.enums.TransactionType;
import java.time.LocalDateTime;

/**
 * WalletLedger entity representing the audit log for wallet transactions.
 *
 * This ledger maintains a complete history of all wallet transactions for auditing purposes.
 * Each entry records the balance before and after a transaction.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@Entity
@Table(name = "wallet_ledger", indexes = {
        @Index(name = "idx_wallet_id", columnList = "wallet_id"),
        @Index(name = "idx_transaction_id", columnList = "transaction_id"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String ledgerId;

    @Column(nullable = false)
    private String walletId;

    @Column(nullable = false)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double balanceBefore;

    @Column(nullable = false)
    private Double balanceAfter;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

