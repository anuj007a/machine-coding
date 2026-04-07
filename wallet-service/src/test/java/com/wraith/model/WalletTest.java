package com.wraith.model;

import com.wraith.enums.WalletStatus;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    void debitAndCredit_updateBalance() {
        Wallet wallet = Wallet.builder().balance(100.0).status(WalletStatus.ACTIVE).locked(false).build();

        wallet.debit(30.0);
        wallet.credit(10.0);

        assertEquals(80.0, wallet.getBalance());
    }

    @Test
    void debit_negativeAmount_throwsException() {
        Wallet wallet = Wallet.builder().balance(100.0).build();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> wallet.debit(-1.0));
        assertTrue(ex.getMessage().contains("negative"));
    }

    @Test
    void debit_insufficientBalance_throwsException() {
        Wallet wallet = Wallet.builder().balance(5.0).build();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> wallet.debit(10.0));
        assertTrue(ex.getMessage().contains("Insufficient"));
    }

    @Test
    void credit_negativeAmount_throwsException() {
        Wallet wallet = Wallet.builder().balance(5.0).build();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> wallet.credit(-10.0));
        assertTrue(ex.getMessage().contains("negative"));
    }

    @Test
    void canBeUsed_trueWhenActiveAndUnlocked() {
        Wallet wallet = Wallet.builder().status(WalletStatus.ACTIVE).locked(false).balance(1.0).build();
        assertTrue(wallet.canBeUsed());
    }

    @Test
    void canBeUsed_falseWhenInactiveOrLocked() {
        Wallet inactive = Wallet.builder().status(WalletStatus.INACTIVE).locked(false).balance(1.0).build();
        Wallet locked = Wallet.builder().status(WalletStatus.ACTIVE).locked(true).balance(1.0).build();

        assertFalse(inactive.canBeUsed());
        assertFalse(locked.canBeUsed());
    }

    @Test
    void callbacks_setAuditFields() throws Exception {
        Wallet wallet = new Wallet();

        Method onCreate = Wallet.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(wallet);

        assertNotNull(wallet.getCreatedAt());
        assertNotNull(wallet.getUpdatedAt());
        assertEquals(WalletStatus.ACTIVE, wallet.getStatus());
        assertFalse(wallet.getLocked());

        LocalDateTime beforeUpdate = wallet.getUpdatedAt();
        Thread.sleep(1);

        Method onUpdate = Wallet.class.getDeclaredMethod("onUpdate");
        onUpdate.setAccessible(true);
        onUpdate.invoke(wallet);

        assertTrue(wallet.getUpdatedAt().isAfter(beforeUpdate) || wallet.getUpdatedAt().isEqual(beforeUpdate));
    }
}

