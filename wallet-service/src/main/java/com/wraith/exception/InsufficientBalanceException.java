package com.wraith.exception;

/**
 * Exception thrown when insufficient balance for transaction.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
public class InsufficientBalanceException extends WalletException {
    public InsufficientBalanceException(String walletId, Double required, Double available) {
        super(String.format("Insufficient balance in wallet %s. Required: %s, Available: %s",
                walletId, required, available), "INSUFFICIENT_BALANCE");
    }
}

