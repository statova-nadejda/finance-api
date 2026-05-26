package com.finance.dto;

import com.finance.model.CategoryType;

public record CategoryResponse(
        Long id,
        String name,
        CategoryType type
) {
}
