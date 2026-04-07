package com.wraith.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumsTest {

    @Test
    void transactionType_valuesExist() {
        assertEquals(TransactionType.CREDIT, TransactionType.valueOf("CREDIT"));
        assertEquals(TransactionType.DEBIT, TransactionType.valueOf("DEBIT"));
    }

    @Test
    void transactionStatus_valuesExist() {
        assertEquals(TransactionStatus.SUCCESS, TransactionStatus.valueOf("SUCCESS"));
        assertEquals(TransactionStatus.FAILED, TransactionStatus.valueOf("FAILED"));
        assertEquals(TransactionStatus.PENDING, TransactionStatus.valueOf("PENDING"));
    }

    @Test
    void walletStatus_valuesExist() {
        assertEquals(WalletStatus.ACTIVE, WalletStatus.valueOf("ACTIVE"));
        assertEquals(WalletStatus.INACTIVE, WalletStatus.valueOf("INACTIVE"));
        assertEquals(WalletStatus.CLOSED, WalletStatus.valueOf("CLOSED"));
    }
}

