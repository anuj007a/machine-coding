package com.wraith.dto;

import lombok.*;
import jakarta.validation.constraints.*;

/**
 * DTO for adding money to a wallet.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddMoneyRequest {

    // Wallet ID is supplied via path variable in controller.
    private String walletId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    private Double amount;

    private String description;
}
