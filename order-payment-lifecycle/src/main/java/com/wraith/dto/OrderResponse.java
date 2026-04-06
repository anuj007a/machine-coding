package com.wraith.dto;

import com.wraith.domain.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private Long amount;
    private String currency;
    private String status;
    private Instant createdAt;
    private List<PaymentResponse> payments;
}

