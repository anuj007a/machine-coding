package com.wraith;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.junit.jupiter.api.Assertions.*;

class WalletServiceApplicationTest {

    @Test
    void applicationAnnotationsPresent() {
        assertTrue(WalletServiceApplication.class.isAnnotationPresent(SpringBootApplication.class));
        assertTrue(WalletServiceApplication.class.isAnnotationPresent(EnableTransactionManagement.class));
    }
}

