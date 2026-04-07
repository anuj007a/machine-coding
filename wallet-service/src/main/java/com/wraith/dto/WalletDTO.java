package com.wraith.dto;

import lombok.*;
import com.wraith.enums.WalletStatus;
import java.time.LocalDateTime;

/**
 * DTO for Wallet response.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {
    private String walletId;
    private String userId;
    private Double balance;
    private WalletStatus status;
    private Boolean locked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

