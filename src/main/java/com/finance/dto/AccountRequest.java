package com.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record AccountRequest(
        @NotBlank(message = "Account name is required")
        String name,

        @NotNull(message = "Balance is required")
        @PositiveOrZero(message = "Balance must be zero or positive")
        Double balance,

        @NotNull(message = "User id is required")
        @Positive(message = "User id must be greater than 0")
        Long userId
) {
}
