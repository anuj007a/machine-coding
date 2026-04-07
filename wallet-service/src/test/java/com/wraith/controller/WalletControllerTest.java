package com.wraith.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wraith.dto.*;
import com.wraith.enums.WalletStatus;
import com.wraith.service.TransactionService;
import com.wraith.service.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WalletService walletService;

    @MockBean
    private TransactionService transactionService;

    @Test
    void createWallet_returnsCreated() throws Exception {
        CreateWalletRequest request = CreateWalletRequest.builder().userId("u1").initialBalance(10.0).build();
        WalletDTO response = WalletDTO.builder().walletId("w1").userId("u1").balance(10.0).build();
        when(walletService.createWallet(any())).thenReturn(response);

        mockMvc.perform(post("/api/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walletId").value("w1"));
    }

    @Test
    void createWallet_validationError_returnsBadRequest() throws Exception {
        CreateWalletRequest invalid = CreateWalletRequest.builder().initialBalance(10.0).build();

        mockMvc.perform(post("/api/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWallet_returnsOk() throws Exception {
        when(walletService.getWalletById("w1")).thenReturn(WalletDTO.builder().walletId("w1").build());

        mockMvc.perform(get("/api/wallets/w1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value("w1"));
    }

    @Test
    void getBalance_returnsOk() throws Exception {
        when(walletService.getBalance("w1")).thenReturn(99.5);

        mockMvc.perform(get("/api/wallets/w1/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("99.5"));
    }

    @Test
    void getUserWallets_returnsOk() throws Exception {
        when(walletService.getWalletsByUserId("u1")).thenReturn(List.of(WalletDTO.builder().walletId("w1").build()));

        mockMvc.perform(get("/api/wallets/user/u1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void updateWalletStatus_returnsOk() throws Exception {
        when(walletService.updateWalletStatus("w1", WalletStatus.INACTIVE))
                .thenReturn(WalletDTO.builder().walletId("w1").status(WalletStatus.INACTIVE).build());

        mockMvc.perform(put("/api/wallets/w1/status").param("status", "INACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void addMoney_setsPathWalletIdAndReturnsOk() throws Exception {
        AddMoneyRequest request = AddMoneyRequest.builder().amount(25.0).description("topup").build();
        when(transactionService.addMoney(any())).thenReturn(TransactionDTO.builder().transactionId("tx1").build());

        mockMvc.perform(post("/api/wallets/w123/add-money")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("tx1"));

        ArgumentCaptor<AddMoneyRequest> captor = ArgumentCaptor.forClass(AddMoneyRequest.class);
        verify(transactionService).addMoney(captor.capture());
        org.junit.jupiter.api.Assertions.assertEquals("w123", captor.getValue().getWalletId());
    }

    @Test
    void transferFunds_returnsOk() throws Exception {
        TransferFundsRequest request = TransferFundsRequest.builder()
                .fromWalletId("w1")
                .toWalletId("w2")
                .amount(5.0)
                .build();

        when(transactionService.transferFunds(any())).thenReturn(TransactionDTO.builder().transactionId("tx2").build());

        mockMvc.perform(post("/api/wallets/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("tx2"));
    }

    @Test
    void getTransaction_returnsOk() throws Exception {
        when(transactionService.getTransactionById("tx1")).thenReturn(TransactionDTO.builder().transactionId("tx1").build());

        mockMvc.perform(get("/api/wallets/transactions/tx1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("tx1"));
    }

    @Test
    void paginatedTransactions_returnsOk() throws Exception {
        when(transactionService.listTransactionsPaginated(eq("w1"), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/wallets/w1/transactions/paginated").param("page", "0").param("size", "10"))
                .andExpect(status().isOk());
    }
}

