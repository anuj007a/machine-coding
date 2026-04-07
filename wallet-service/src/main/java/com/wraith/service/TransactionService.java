package com.wraith.service;

import com.wraith.dto.AddMoneyRequest;
import com.wraith.dto.TransactionDTO;
import com.wraith.dto.TransferFundsRequest;
import com.wraith.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for Transaction Service.
 *
 * Defines all transaction-related business operations.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
public interface TransactionService {

    /**
     * Add money to a wallet.
     */
    TransactionDTO addMoney(AddMoneyRequest request);

    /**
     * Transfer funds between two wallets.
     */
    TransactionDTO transferFunds(TransferFundsRequest request);

    /**
     * Retrieve transaction details by transaction ID.
     */
    TransactionDTO getTransactionById(String transactionId);

    /**
     * List transactions for a wallet with optional filters.
     */
    List<TransactionDTO> listTransactions(String walletId, LocalDateTime startDate,
                                          LocalDateTime endDate, TransactionType type);

    /**
     * List transactions for a wallet with pagination.
     */
    Page<TransactionDTO> listTransactionsPaginated(String walletId, Pageable pageable);
}

