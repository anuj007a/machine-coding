package com.wraith.dto;

import lombok.*;
import jakarta.validation.constraints.*;

/**
 * DTO for transferring funds between wallets.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferFundsRequest {

    @NotBlank(message = "From wallet ID is required")
    private String fromWalletId;

    @NotBlank(message = "To wallet ID is required")
    private String toWalletId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    private Double amount;

    private String description;
}

