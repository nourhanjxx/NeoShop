package com.elsebaey.ecommerce.order;

import java.math.BigDecimal;

public record OrderResponse(
    Integer orderId,
    String reference,
    BigDecimal totalAmount,
    PaymentMethod paymentMethod,
    String customerId
) {
}
