package com.elsebaey.ecommerce.product;

import java.math.BigDecimal;

public record ProductPurchaseResponse(
        Integer productId,
        String name,
        String description,
        double quantity,
        BigDecimal price
) {
}
