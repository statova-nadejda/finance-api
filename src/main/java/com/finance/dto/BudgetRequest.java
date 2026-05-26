package com.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BudgetRequest(
        @NotNull(message = "Limit amount is required")
        @Positive(message = "Limit amount must be positive")
        Double limitAmount,

        @NotBlank(message = "Month is required")
        String month,

        @NotNull(message = "User id is required")
        @Positive(message = "User id must be greater than 0")
        Long userId,

        @NotNull(message = "Category id is required")
        @Positive(message = "Category id must be greater than 0")
        Long categoryId
) {
}
