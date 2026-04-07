package com.wraith.service.impl;

import com.wraith.dto.CreateWalletRequest;
import com.wraith.dto.WalletDTO;
import com.wraith.enums.WalletStatus;
import com.wraith.exception.WalletException;
import com.wraith.exception.WalletNotFoundException;
import com.wraith.model.User;
import com.wraith.model.Wallet;
import com.wraith.repository.UserRepository;
import com.wraith.repository.WalletRepository;
import com.wraith.repository.TransactionRepository;
import com.wraith.repository.WalletLedgerRepository;
import com.wraith.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of IWalletService.
 *
 * Handles all wallet business logic including creation, updates, and balance checks.
 * All operations maintain ACID properties and audit trails.
 * Provides pessimistic and optimistic locking for concurrent transaction safety.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final WalletLedgerRepository walletLedgerRepository;

    /**
     * Create a new wallet for a user with initial balance.
     * Validates that user exists before creating wallet.
     *
     * @param request CreateWalletRequest with userId and initialBalance
     * @return WalletDTO of created wallet
     * @throws WalletException if user not found
     */
    @Override
    @Transactional
    public WalletDTO createWallet(CreateWalletRequest request) {
        log.info("Creating wallet for user: {}", request.getUserId());

        // Verify user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new WalletException("User not found: " + request.getUserId(), "USER_NOT_FOUND"));

        // Create new wallet with initial balance
        Wallet wallet = Wallet.builder()
                .userId(request.getUserId())
                .balance(request.getInitialBalance() != null ? request.getInitialBalance() : 0.0)
                .status(WalletStatus.ACTIVE)
                .locked(false)
                .build();

        Wallet savedWallet = walletRepository.save(wallet);
        log.info("Wallet created successfully with ID: {} for user: {}", savedWallet.getWalletId(), request.getUserId());

        return toWalletDTO(savedWallet);
    }

    /**
     * Retrieve wallet details by wallet ID.
     *
     * @param walletId the wallet ID
     * @return WalletDTO with wallet details
     * @throws WalletNotFoundException if wallet not found
     */
    @Override
    @Transactional(readOnly = true)
    public WalletDTO getWalletById(String walletId) {
        log.debug("Fetching wallet details for ID: {}", walletId);

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        return toWalletDTO(wallet);
    }

    /**
     * Get current balance of a wallet.
     *
     * @param walletId the wallet ID
     * @return current balance
     * @throws WalletNotFoundException if wallet not found
     */
    @Override
    @Transactional(readOnly = true)
    public Double getBalance(String walletId) {
        log.debug("Fetching balance for wallet: {}", walletId);

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        return wallet.getBalance();
    }

    /**
     * Get all wallets belonging to a user.
     *
     * @param userId the user ID
     * @return List of WalletDTOs for the user
     */
    @Override
    @Transactional(readOnly = true)
    public List<WalletDTO> getWalletsByUserId(String userId) {
        log.debug("Fetching all wallets for user: {}", userId);

        return walletRepository.findByUserId(userId).stream()
                .map(this::toWalletDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update wallet status (ACTIVE -> INACTIVE -> CLOSED).
     * Prevents closing wallet with non-zero balance.
     * Uses optimistic locking to detect concurrent modifications.
     *
     * @param walletId the wallet ID
     * @param newStatus the new wallet status
     * @return WalletDTO with updated status
     * @throws WalletNotFoundException if wallet not found
     * @throws WalletException if wallet has balance and being closed
     */
    @Override
    @Transactional
    public WalletDTO updateWalletStatus(String walletId, WalletStatus newStatus) {
        log.info("Updating wallet status for ID: {} to: {}", walletId, newStatus);

        // Use optimistic locking to ensure concurrent safe updates
        Wallet wallet = walletRepository.findByIdWithLock(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        // Business rule: Cannot close wallet with non-zero balance
        if (newStatus == WalletStatus.CLOSED && wallet.getBalance() > 0) {
            log.warn("Cannot close wallet {} with balance: {}", walletId, wallet.getBalance());
            throw new WalletException("Cannot close wallet with non-zero balance. Current balance: " + wallet.getBalance(),
                    "INVALID_WALLET_STATE");
        }

        wallet.setStatus(newStatus);
        Wallet updatedWallet = walletRepository.save(wallet);
        log.info("Wallet status updated successfully. Wallet ID: {}, New Status: {}", walletId, newStatus);

        return toWalletDTO(updatedWallet);
    }

    /**
     * Check if wallet has sufficient balance for a transaction.
     *
     * @param walletId the wallet ID
     * @param amount the amount to check
     * @return true if balance >= amount, false otherwise
     * @throws WalletNotFoundException if wallet not found
     */
    @Override
    @Transactional(readOnly = true)
    public boolean hasSufficientBalance(String walletId, Double amount) {
        log.debug("Checking balance for wallet: {} against amount: {}", walletId, amount);

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        return wallet.hasSufficientBalance(amount);
    }

    /**
     * Convert Wallet entity to WalletDTO.
     *
     * @param wallet the wallet entity
     * @return WalletDTO representation
     */
    private WalletDTO toWalletDTO(Wallet wallet) {
        return WalletDTO.builder()
                .walletId(wallet.getWalletId())
                .userId(wallet.getUserId())
                .balance(wallet.getBalance())
                .status(wallet.getStatus())
                .locked(wallet.getLocked())
                .createdAt(wallet.getCreatedAt())
                .updatedAt(wallet.getUpdatedAt())
                .build();
    }
}

