package com.finance.dto;

public record AccountResponse(
        Long id,
        String name,
        Double balance,
        Long userId
) {
}
