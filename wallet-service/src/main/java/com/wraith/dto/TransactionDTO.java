package com.wraith.dto;

import lombok.*;
import com.wraith.enums.TransactionType;
import com.wraith.enums.TransactionStatus;
import java.time.LocalDateTime;

/**
 * DTO for Transaction response.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String transactionId;
    private String fromWalletId;
    private String toWalletId;
    private Double amount;
    private TransactionType type;
    private TransactionStatus status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

