package com.wraith.repository;

import com.wraith.model.Wallet;
import com.wraith.enums.WalletStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Wallet entity.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {

    /**
     * Find all wallets for a specific user.
     */
    List<Wallet> findByUserId(String userId);

    /**
     * Find all active wallets for a user.
     */
    List<Wallet> findByUserIdAndStatus(String userId, WalletStatus status);

    /**
     * Find wallet with optimistic locking for safe concurrent updates.
     */
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT w FROM Wallet w WHERE w.walletId = :walletId")
    Optional<Wallet> findByIdWithLock(@Param("walletId") String walletId);

    /**
     * Find wallet with pessimistic locking for exclusive access.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.walletId = :walletId")
    Optional<Wallet> findByIdWithPessimisticLock(@Param("walletId") String walletId);
}

