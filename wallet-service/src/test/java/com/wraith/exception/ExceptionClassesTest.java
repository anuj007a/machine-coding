package com.wraith.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionClassesTest {

    @Test
    void walletException_defaultErrorCode() {
        WalletException ex = new WalletException("boom");
        assertEquals("WALLET_ERROR", ex.getErrorCode());
        assertEquals("boom", ex.getMessage());
    }

    @Test
    void walletNotFoundException_hasCodeAndMessage() {
        WalletNotFoundException ex = new WalletNotFoundException("w123");
        assertEquals("WALLET_NOT_FOUND", ex.getErrorCode());
        assertTrue(ex.getMessage().contains("w123"));
    }

    @Test
    void insufficientBalanceException_hasCodeAndDetails() {
        InsufficientBalanceException ex = new InsufficientBalanceException("w1", 100.0, 20.0);
        assertEquals("INSUFFICIENT_BALANCE", ex.getErrorCode());
        assertTrue(ex.getMessage().contains("Required"));
        assertTrue(ex.getMessage().contains("Available"));
    }
}

