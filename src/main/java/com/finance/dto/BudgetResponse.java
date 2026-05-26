package com.finance.dto;

public record BudgetResponse(
        Long id,
        Double limitAmount,
        String month,
        Long userId,
        Long categoryId
) {
}
