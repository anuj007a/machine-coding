package com.wraith.exception;

/**
 * Base exception for wallet service.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
public class WalletException extends RuntimeException {

    private String errorCode;

    public WalletException(String message) {
        super(message);
        this.errorCode = "WALLET_ERROR";
    }

    public WalletException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "WALLET_ERROR";
    }

    public WalletException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public WalletException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

