package com.wraith.dto;

import lombok.*;
import jakarta.validation.constraints.*;

/**
 * DTO for creating a new wallet.
 *
 * @author Wallet Service Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance cannot be negative")
    private Double initialBalance;
}

