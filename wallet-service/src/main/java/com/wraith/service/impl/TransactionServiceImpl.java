package com.wraith.service.impl;

import com.wraith.dto.AddMoneyRequest;
import com.wraith.dto.TransactionDTO;
import com.wraith.dto.TransferFundsRequest;
import com.wraith.enums.TransactionStatus;
import com.wraith.enums.TransactionType;
import com.wraith.exception.WalletException;
import com.wraith.exception.WalletNotFoundException;
import com.wraith.exception.InsufficientBalanceException;
import com.wraith.model.Transaction;
import com.wraith.model.Wallet;
import com.wraith.model.WalletLedger;
import com.wraith.repository.TransactionRepository;
import com.wraith.repository.WalletRepository;
import com.wraith.repository.WalletLedgerRepository;
import com.wraith.service.TransactionService;
import com.wraith.enums.WalletStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ITransactionService.
 *
 * Handles all transaction business logic including adding money, transfers, and transaction history.
 * Provides comprehensive validation and audit trails via WalletLedger.
 * Uses pessimistic locking to ensure atomic fund transfers.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final WalletLedgerRepository walletLedgerRepository;

    /**
     * Add money to a wallet.
     * Creates a CREDIT transaction and updates wallet balance.
     * Records transaction in ledger for audit trail.
     *
     * @param request AddMoneyRequest with walletId and amount
     * @return TransactionDTO of completed transaction
     * @throws WalletNotFoundException if wallet not found
     * @throws WalletException if wallet cannot be used or amount is invalid
     */
    @Override
    @Transactional
    public TransactionDTO addMoney(AddMoneyRequest request) {
        log.info("Adding money to wallet: {}, amount: {}", request.getWalletId(), request.getAmount());

        // Validate amount
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new WalletException("Amount must be greater than 0", "INVALID_AMOUNT");
        }

        // Lock and fetch wallet using pessimistic locking
        Wallet wallet = walletRepository.findByIdWithPessimisticLock(request.getWalletId())
                .orElseThrow(() -> new WalletNotFoundException(request.getWalletId()));

        // Validate wallet state
        if (!wallet.canBeUsed()) {
            throw new WalletException("Wallet cannot be used for transactions. Status: " + wallet.getStatus(),
                    "WALLET_NOT_USABLE");
        }

        // Create transaction record
        Transaction transaction = Transaction.builder()
                .toWalletId(request.getWalletId())
                .amount(request.getAmount())
                .type(TransactionType.CREDIT)
                .status(TransactionStatus.PENDING)
                .description(request.getDescription() != null ? request.getDescription() : "Add money")
                .build();

        try {
            // Record balance before update
            Double balanceBefore = wallet.getBalance();

            // Credit wallet
            wallet.credit(request.getAmount());

            // Save transaction as SUCCESS
            transaction.setStatus(TransactionStatus.SUCCESS);
            Transaction savedTransaction = transactionRepository.save(transaction);

            // Update wallet in database
            walletRepository.save(wallet);

            // Record in ledger
            recordLedger(wallet.getWalletId(), savedTransaction.getTransactionId(),
                    TransactionType.CREDIT, request.getAmount(), balanceBefore, wallet.getBalance());

            log.info("Money added successfully. Wallet: {}, Amount: {}, Transaction: {}",
                    request.getWalletId(), request.getAmount(), savedTransaction.getTransactionId());

            return toTransactionDTO(savedTransaction);
        } catch (WalletException e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            throw e;
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            log.error("Failed to add money to wallet: {}", request.getWalletId(), e);
            throw new WalletException("Failed to add money: " + e.getMessage(), "TRANSACTION_FAILED", e);
        }
    }

    /**
     * Transfer funds between two wallets.
     * Validates both wallets, checks sufficient balance, and performs atomic transfer.
     * Prevents self-transfer and records transaction in ledger for both wallets.
     * Uses pessimistic locking for both wallets to prevent race conditions.
     *
     * @param request TransferFundsRequest with from/to walletId and amount
     * @return TransactionDTO of completed transfer
     * @throws WalletNotFoundException if either wallet not found
     * @throws WalletException if validation fails (self-transfer, insufficient balance, wallet not usable)
     */
    @Override
    @Transactional
    public TransactionDTO transferFunds(TransferFundsRequest request) {
        log.info("Transferring funds from {} to {}, amount: {}",
                request.getFromWalletId(), request.getToWalletId(), request.getAmount());

        // Validate amount
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new WalletException("Amount must be greater than 0", "INVALID_AMOUNT");
        }

        // Prevent self-transfer
        if (request.getFromWalletId().equals(request.getToWalletId())) {
            throw new WalletException("Cannot transfer to the same wallet", "SELF_TRANSFER_NOT_ALLOWED");
        }

        // Fetch wallets with pessimistic locking (exclusive access)
        // Lock fromWallet first to avoid deadlock
        Wallet fromWallet = walletRepository.findByIdWithPessimisticLock(request.getFromWalletId())
                .orElseThrow(() -> new WalletNotFoundException(request.getFromWalletId()));

        Wallet toWallet = walletRepository.findByIdWithPessimisticLock(request.getToWalletId())
                .orElseThrow(() -> new WalletNotFoundException(request.getToWalletId()));

        // Validate source wallet state
        if (!fromWallet.canBeUsed()) {
            throw new WalletException("Source wallet cannot be used for transactions. Status: " + fromWallet.getStatus(),
                    "SOURCE_WALLET_NOT_USABLE");
        }

        // Validate destination wallet state (can be INACTIVE but not CLOSED)
        if (toWallet.getStatus().equals(WalletStatus.CLOSED)) {
            throw new WalletException("Destination wallet is closed", "DESTINATION_WALLET_CLOSED");
        }

        // Validate sufficient balance
        if (!fromWallet.hasSufficientBalance(request.getAmount())) {
            throw new InsufficientBalanceException(request.getFromWalletId(),
                    request.getAmount(), fromWallet.getBalance());
        }

        // Create transaction record
        Transaction transaction = Transaction.builder()
                .fromWalletId(request.getFromWalletId())
                .toWalletId(request.getToWalletId())
                .amount(request.getAmount())
                .type(TransactionType.DEBIT)  // From perspective of source wallet
                .status(TransactionStatus.PENDING)
                .description(request.getDescription() != null ? request.getDescription() : "Fund transfer")
                .build();

        try {
            // Record balances before update
            Double fromBalanceBefore = fromWallet.getBalance();
            Double toBalanceBefore = toWallet.getBalance();

            // Debit from source wallet
            fromWallet.debit(request.getAmount());

            // Credit to destination wallet
            toWallet.credit(request.getAmount());

            // Save transaction as SUCCESS
            transaction.setStatus(TransactionStatus.SUCCESS);
            Transaction savedTransaction = transactionRepository.save(transaction);

            // Update both wallets in database
            walletRepository.save(fromWallet);
            walletRepository.save(toWallet);

            // Record in ledgers for both wallets
            recordLedger(fromWallet.getWalletId(), savedTransaction.getTransactionId(),
                    TransactionType.DEBIT, request.getAmount(), fromBalanceBefore, fromWallet.getBalance());

            recordLedger(toWallet.getWalletId(), savedTransaction.getTransactionId(),
                    TransactionType.CREDIT, request.getAmount(), toBalanceBefore, toWallet.getBalance());

            log.info("Funds transferred successfully. From: {}, To: {}, Amount: {}, Transaction: {}",
                    request.getFromWalletId(), request.getToWalletId(), request.getAmount(), savedTransaction.getTransactionId());

            return toTransactionDTO(savedTransaction);
        } catch (WalletException e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            throw e;
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            log.error("Failed to transfer funds from {} to {}",
                    request.getFromWalletId(), request.getToWalletId(), e);
            throw new WalletException("Failed to transfer funds: " + e.getMessage(), "TRANSACTION_FAILED", e);
        }
    }

    /**
     * Retrieve transaction details by transaction ID.
     *
     * @param transactionId the transaction ID
     * @return TransactionDTO with transaction details
     * @throws WalletException if transaction not found
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(String transactionId) {
        log.debug("Fetching transaction: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new WalletException("Transaction not found: " + transactionId, "TRANSACTION_NOT_FOUND"));

        return toTransactionDTO(transaction);
    }

    /**
     * List transactions for a wallet with optional filters by date range and type.
     *
     * @param walletId the wallet ID
     * @param startDate optional start date filter
     * @param endDate optional end date filter
     * @param type optional transaction type filter (CREDIT/DEBIT)
     * @return List of TransactionDTOs matching the criteria
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> listTransactions(String walletId, LocalDateTime startDate,
                                                  LocalDateTime endDate, TransactionType type) {
        log.debug("Listing transactions for wallet: {} with filters", walletId);

        List<Transaction> transactions;

        if (startDate != null && endDate != null) {
            transactions = transactionRepository.findByWalletIdAndDateRange(walletId, startDate, endDate);
        } else if (type != null) {
            transactions = transactionRepository.findByWalletIdAndType(walletId, type);
        } else {
            transactions = transactionRepository.findByWalletId(walletId);
        }

        return transactions.stream()
                .map(this::toTransactionDTO)
                .collect(Collectors.toList());
    }

    /**
     * List transactions for a wallet with pagination support.
     * Useful for displaying transactions in pages on UI.
     *
     * @param walletId the wallet ID
     * @param pageable pagination information (page, size, sort)
     * @return Page of TransactionDTOs
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDTO> listTransactionsPaginated(String walletId, Pageable pageable) {
        log.debug("Listing transactions for wallet (paginated): {}", walletId);

        return transactionRepository.findByWalletIdPaginated(walletId, pageable)
                .map(this::toTransactionDTO);
    }

    /**
     * Record transaction in wallet ledger for audit trail.
     * Maintains complete history of balance changes.
     *
     * @param walletId the wallet ID
     * @param transactionId the transaction ID
     * @param type the transaction type (CREDIT/DEBIT)
     * @param amount the transaction amount
     * @param balanceBefore balance before transaction
     * @param balanceAfter balance after transaction
     */
    private void recordLedger(String walletId, String transactionId, TransactionType type,
                              Double amount, Double balanceBefore, Double balanceAfter) {
        WalletLedger ledger = WalletLedger.builder()
                .walletId(walletId)
                .transactionId(transactionId)
                .transactionType(type)
                .amount(amount)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .build();

        walletLedgerRepository.save(ledger);
        log.debug("Ledger entry recorded for wallet: {}, transaction: {}", walletId, transactionId);
    }

    /**
     * Convert Transaction entity to TransactionDTO.
     *
     * @param transaction the transaction entity
     * @return TransactionDTO representation
     */
    private TransactionDTO toTransactionDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .transactionId(transaction.getTransactionId())
                .fromWalletId(transaction.getFromWalletId())
                .toWalletId(transaction.getToWalletId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .status(transaction.getStatus())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}

