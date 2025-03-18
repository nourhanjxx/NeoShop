package com.elsebaey.ecommerce.kafka;

import com.elsebaey.ecommerce.customer.CustomerResponse;
import com.elsebaey.ecommerce.order.PaymentMethod;
import com.elsebaey.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
