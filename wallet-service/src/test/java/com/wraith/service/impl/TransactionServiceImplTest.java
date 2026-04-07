package com.wraith.service.impl;

import com.wraith.dto.AddMoneyRequest;
import com.wraith.dto.TransactionDTO;
import com.wraith.dto.TransferFundsRequest;
import com.wraith.enums.TransactionStatus;
import com.wraith.enums.TransactionType;
import com.wraith.enums.WalletStatus;
import com.wraith.exception.InsufficientBalanceException;
import com.wraith.exception.WalletException;
import com.wraith.exception.WalletNotFoundException;
import com.wraith.model.Transaction;
import com.wraith.model.Wallet;
import com.wraith.repository.TransactionRepository;
import com.wraith.repository.WalletLedgerRepository;
import com.wraith.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private WalletLedgerRepository walletLedgerRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void addMoney_invalidAmount_throwsException() {
        AddMoneyRequest request = AddMoneyRequest.builder().walletId("w1").amount(0.0).build();
        WalletException ex = assertThrows(WalletException.class, () -> transactionService.addMoney(request));
        assertEquals("INVALID_AMOUNT", ex.getErrorCode());
    }

    @Test
    void addMoney_walletNotFound_throwsException() {
        AddMoneyRequest request = AddMoneyRequest.builder().walletId("w1").amount(10.0).build();
        when(walletRepository.findByIdWithPessimisticLock("w1")).thenReturn(Optional.empty());
        assertThrows(WalletNotFoundException.class, () -> transactionService.addMoney(request));
    }

    @Test
    void addMoney_success() {
        Wallet wallet = Wallet.builder().walletId("w1").balance(100.0).status(WalletStatus.ACTIVE).locked(false).build();
        AddMoneyRequest request = AddMoneyRequest.builder().walletId("w1").amount(25.0).description("topup").build();

        when(walletRepository.findByIdWithPessimisticLock("w1")).thenReturn(Optional.of(wallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            if (t.getTransactionId() == null) {
                t.setTransactionId("tx1");
            }
            return t;
        });
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransactionDTO dto = transactionService.addMoney(request);

        assertEquals("tx1", dto.getTransactionId());
        assertEquals(TransactionStatus.SUCCESS, dto.getStatus());
        verify(walletLedgerRepository, times(1)).save(any());
    }

    @Test
    void transferFunds_selfTransfer_throwsException() {
        TransferFundsRequest request = TransferFundsRequest.builder()
                .fromWalletId("w1")
                .toWalletId("w1")
                .amount(10.0)
                .build();

        WalletException ex = assertThrows(WalletException.class, () -> transactionService.transferFunds(request));
        assertEquals("SELF_TRANSFER_NOT_ALLOWED", ex.getErrorCode());
    }

    @Test
    void transferFunds_insufficientBalance_throwsException() {
        Wallet from = Wallet.builder().walletId("w1").balance(10.0).status(WalletStatus.ACTIVE).locked(false).build();
        Wallet to = Wallet.builder().walletId("w2").balance(5.0).status(WalletStatus.ACTIVE).locked(false).build();

        when(walletRepository.findByIdWithPessimisticLock("w1")).thenReturn(Optional.of(from));
        when(walletRepository.findByIdWithPessimisticLock("w2")).thenReturn(Optional.of(to));

        TransferFundsRequest request = TransferFundsRequest.builder().fromWalletId("w1").toWalletId("w2").amount(20.0).build();

        assertThrows(InsufficientBalanceException.class, () -> transactionService.transferFunds(request));
    }

    @Test
    void transferFunds_destinationClosed_throwsException() {
        Wallet from = Wallet.builder().walletId("w1").balance(100.0).status(WalletStatus.ACTIVE).locked(false).build();
        Wallet to = Wallet.builder().walletId("w2").balance(5.0).status(WalletStatus.CLOSED).locked(false).build();

        when(walletRepository.findByIdWithPessimisticLock("w1")).thenReturn(Optional.of(from));
        when(walletRepository.findByIdWithPessimisticLock("w2")).thenReturn(Optional.of(to));

        TransferFundsRequest request = TransferFundsRequest.builder().fromWalletId("w1").toWalletId("w2").amount(20.0).build();

        WalletException ex = assertThrows(WalletException.class, () -> transactionService.transferFunds(request));
        assertEquals("DESTINATION_WALLET_CLOSED", ex.getErrorCode());
    }

    @Test
    void transferFunds_success() {
        Wallet from = Wallet.builder().walletId("w1").balance(100.0).status(WalletStatus.ACTIVE).locked(false).build();
        Wallet to = Wallet.builder().walletId("w2").balance(10.0).status(WalletStatus.ACTIVE).locked(false).build();

        when(walletRepository.findByIdWithPessimisticLock("w1")).thenReturn(Optional.of(from));
        when(walletRepository.findByIdWithPessimisticLock("w2")).thenReturn(Optional.of(to));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            if (t.getTransactionId() == null) {
                t.setTransactionId("tx2");
            }
            return t;
        });
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransferFundsRequest request = TransferFundsRequest.builder().fromWalletId("w1").toWalletId("w2").amount(40.0).build();
        TransactionDTO dto = transactionService.transferFunds(request);

        assertEquals("tx2", dto.getTransactionId());
        assertEquals(TransactionStatus.SUCCESS, dto.getStatus());
        verify(walletLedgerRepository, times(2)).save(any());
    }

    @Test
    void getTransactionById_notFound_throwsException() {
        when(transactionRepository.findById("tx404")).thenReturn(Optional.empty());
        WalletException ex = assertThrows(WalletException.class, () -> transactionService.getTransactionById("tx404"));
        assertEquals("TRANSACTION_NOT_FOUND", ex.getErrorCode());
    }

    @Test
    void listTransactions_usesDateRangeFilter() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        when(transactionRepository.findByWalletIdAndDateRange("w1", start, end)).thenReturn(List.of());

        List<TransactionDTO> result = transactionService.listTransactions("w1", start, end, null);

        assertNotNull(result);
        verify(transactionRepository).findByWalletIdAndDateRange("w1", start, end);
    }

    @Test
    void listTransactionsPaginated_returnsPage() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Transaction> page = new PageImpl<>(List.of());
        when(transactionRepository.findByWalletIdPaginated("w1", pageable)).thenReturn(page);

        Page<TransactionDTO> result = transactionService.listTransactionsPaginated("w1", pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }
}

