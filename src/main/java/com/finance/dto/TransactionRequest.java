package com.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record TransactionRequest(
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        Double amount,

        @NotNull(message = "Date is required")
        LocalDate date,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Account id is required")
        @Positive(message = "Account id must be greater than 0")
        Long accountId,

        @NotNull(message = "Category id is required")
        @Positive(message = "Category id must be greater than 0")
        Long categoryId
) {
}
