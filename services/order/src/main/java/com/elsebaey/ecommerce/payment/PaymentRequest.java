package com.elsebaey.ecommerce.payment;

import com.elsebaey.ecommerce.customer.CustomerResponse;
import com.elsebaey.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
