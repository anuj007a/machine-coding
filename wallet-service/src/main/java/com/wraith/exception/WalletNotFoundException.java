package com.wraith.exception;

/**
 * Exception thrown when wallet is not found.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
public class WalletNotFoundException extends WalletException {
    public WalletNotFoundException(String walletId) {
        super("Wallet not found: " + walletId, "WALLET_NOT_FOUND");
    }
}

