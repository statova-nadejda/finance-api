package com.finance.dto;

import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        Double amount,
        LocalDate date,
        String description,
        Long accountId,
        Long categoryId
) {
}
