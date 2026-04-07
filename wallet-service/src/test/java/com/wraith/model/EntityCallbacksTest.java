package com.wraith.model;

import com.wraith.enums.TransactionStatus;
import com.wraith.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EntityCallbacksTest {

    @Test
    void transaction_callbacksPopulateTimestamps() throws Exception {
        Transaction transaction = Transaction.builder()
                .amount(10.0)
                .type(TransactionType.CREDIT)
                .status(TransactionStatus.PENDING)
                .toWalletId("w1")
                .build();

        Method onCreate = Transaction.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(transaction);

        assertNotNull(transaction.getCreatedAt());
        assertNotNull(transaction.getUpdatedAt());

        LocalDateTime before = transaction.getUpdatedAt();
        Method onUpdate = Transaction.class.getDeclaredMethod("onUpdate");
        onUpdate.setAccessible(true);
        onUpdate.invoke(transaction);

        assertTrue(transaction.getUpdatedAt().isAfter(before) || transaction.getUpdatedAt().isEqual(before));
    }

    @Test
    void user_callbacksPopulateTimestamps() throws Exception {
        User user = User.builder().email("u@x.com").name("User").build();

        Method onCreate = User.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(user);

        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());

        LocalDateTime before = user.getUpdatedAt();
        Method onUpdate = User.class.getDeclaredMethod("onUpdate");
        onUpdate.setAccessible(true);
        onUpdate.invoke(user);

        assertTrue(user.getUpdatedAt().isAfter(before) || user.getUpdatedAt().isEqual(before));
    }

    @Test
    void walletLedger_callbackPopulatesCreatedAt() throws Exception {
        WalletLedger ledger = WalletLedger.builder()
                .walletId("w1")
                .transactionId("tx1")
                .transactionType(TransactionType.DEBIT)
                .amount(5.0)
                .balanceBefore(10.0)
                .balanceAfter(5.0)
                .build();

        Method onCreate = WalletLedger.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(ledger);

        assertNotNull(ledger.getCreatedAt());
    }
}

