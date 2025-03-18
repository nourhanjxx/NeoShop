package com.elsebaey.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        Integer id,
        @NotNull(message = "name is required")
        String name,
        @NotNull(message = "description is required")
        String description,
        @Positive(message = "availableQuantity must be positive")
        double availableQuantity,
        @Positive(message = "price must be positive")
        BigDecimal price,
        @NotNull(message = "Product category is required")
        Integer categoryId
) {
}
