package com.wraith.model;

import jakarta.persistence.*;
import lombok.*;
import com.wraith.enums.WalletStatus;
import java.time.LocalDateTime;

/**
 * Wallet entity representing a wallet owned by a user.
 *
 * A wallet can have multiple transactions and maintains a balance.
 * Balance can never go negative.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@Entity
@Table(name = "wallets", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String walletId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Double balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletStatus status;

    @Column(name = "is_locked", nullable = false)
    private Boolean locked;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        locked = false;
        status = WalletStatus.ACTIVE;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if the wallet has sufficient balance for a transaction.
     *
     * @param amount the amount to check
     * @return true if balance is sufficient, false otherwise
     */
    public boolean hasSufficientBalance(Double amount) {
        return balance >= amount;
    }

    /**
     * Deducts amount from wallet balance.
     *
     * @param amount the amount to deduct
     * @throws IllegalArgumentException if amount is negative or balance becomes negative
     */
    public void debit(Double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Debit amount cannot be negative");
        }
        if (!hasSufficientBalance(amount)) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        this.balance -= amount;
    }

    /**
     * Adds amount to wallet balance.
     *
     * @param amount the amount to add
     * @throws IllegalArgumentException if amount is negative
     */
    public void credit(Double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Credit amount cannot be negative");
        }
        this.balance += amount;
    }

    /**
     * Checks if wallet is active.
     *
     * @return true if wallet status is ACTIVE, false otherwise
     */
    public boolean isActive() {
        return WalletStatus.ACTIVE.equals(status);
    }

    /**
     * Checks if wallet is locked.
     *
     * @return true if wallet is locked, false otherwise
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Checks if wallet can be used for transactions.
     *
     * @return true if wallet is active and not locked, false otherwise
     */
    public boolean canBeUsed() {
        return isActive() && !isLocked();
    }
}

