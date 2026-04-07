package com.wraith.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DtoValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    @Test
    void createWalletRequest_requiresUserIdAndNonNegativeBalance() {
        CreateWalletRequest request = CreateWalletRequest.builder().userId("").initialBalance(-1.0).build();
        Set<ConstraintViolation<CreateWalletRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void addMoneyRequest_requiresPositiveAmount() {
        AddMoneyRequest request = AddMoneyRequest.builder().amount(0.0).build();
        Set<ConstraintViolation<AddMoneyRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void transferFundsRequest_requiresWalletsAndPositiveAmount() {
        TransferFundsRequest request = TransferFundsRequest.builder().fromWalletId("").toWalletId("").amount(0.0).build();
        Set<ConstraintViolation<TransferFundsRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void responseDtos_canBeBuilt() {
        WalletDTO walletDTO = WalletDTO.builder().walletId("w1").userId("u1").balance(10.0).build();
        TransactionDTO tx = TransactionDTO.builder().transactionId("t1").amount(5.0).build();

        assertEquals("w1", walletDTO.getWalletId());
        assertEquals("t1", tx.getTransactionId());
    }
}
