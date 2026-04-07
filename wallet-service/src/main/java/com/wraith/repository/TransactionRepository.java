package com.wraith.repository;

import com.wraith.model.Transaction;
import com.wraith.enums.TransactionStatus;
import com.wraith.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Transaction entity.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    /**
     * Find all transactions for a specific wallet (both sent and received).
     */
    @Query("SELECT t FROM Transaction t WHERE t.fromWalletId = :walletId OR t.toWalletId = :walletId")
    List<Transaction> findByWalletId(@Param("walletId") String walletId);

    /**
     * Find transactions with pagination for a wallet.
     */
    @Query("SELECT t FROM Transaction t WHERE t.fromWalletId = :walletId OR t.toWalletId = :walletId ORDER BY t.createdAt DESC")
    Page<Transaction> findByWalletIdPaginated(@Param("walletId") String walletId, Pageable pageable);

    /**
     * Find transactions within a date range for a wallet.
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromWalletId = :walletId OR t.toWalletId = :walletId) " +
            "AND t.createdAt BETWEEN :startDate AND :endDate")
    List<Transaction> findByWalletIdAndDateRange(@Param("walletId") String walletId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    /**
     * Find transactions by type for a wallet.
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromWalletId = :walletId OR t.toWalletId = :walletId) " +
            "AND t.type = :type")
    List<Transaction> findByWalletIdAndType(@Param("walletId") String walletId, @Param("type") TransactionType type);

    /**
     * Find transactions by status for a wallet.
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromWalletId = :walletId OR t.toWalletId = :walletId) " +
            "AND t.status = :status")
    List<Transaction> findByWalletIdAndStatus(@Param("walletId") String walletId, @Param("status") TransactionStatus status);

    /**
     * Find outgoing transactions from a wallet.
     */
    List<Transaction> findByFromWalletId(String fromWalletId);

    /**
     * Find incoming transactions to a wallet.
     */
    List<Transaction> findByToWalletId(String toWalletId);
}

