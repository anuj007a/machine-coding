package com.wraith.repository;

import com.wraith.model.WalletLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for WalletLedger entity.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@Repository
public interface WalletLedgerRepository extends JpaRepository<WalletLedger, String> {

    /**
     * Find all ledger entries for a wallet.
     */
    List<WalletLedger> findByWalletId(String walletId);

    /**
     * Find ledger entries for a wallet within a date range.
     */
    List<WalletLedger> findByWalletIdAndCreatedAtBetween(String walletId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find ledger entry by transaction ID.
     */
    WalletLedger findByTransactionId(String transactionId);
}

