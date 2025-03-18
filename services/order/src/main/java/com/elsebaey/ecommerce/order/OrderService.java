package com.elsebaey.ecommerce.order;

import com.elsebaey.ecommerce.customer.CustomerClient;
import com.elsebaey.ecommerce.exception.BusinessException;
import com.elsebaey.ecommerce.kafka.OrderConfirmation;
import com.elsebaey.ecommerce.kafka.OrderProducer;
import com.elsebaey.ecommerce.orderline.OrderLineRequest;
import com.elsebaey.ecommerce.orderline.OrderLineService;
import com.elsebaey.ecommerce.payment.PaymentClient;
import com.elsebaey.ecommerce.payment.PaymentRequest;
import com.elsebaey.ecommerce.product.ProductClient;
import com.elsebaey.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    private final OrderRepository repository;

    public Integer createOrder(OrderRequest request) {
        var customer = customerClient.findById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer with id " + request.customerId()));

        var purchasedProducts = productClient.purchaseProducts(request.products());

        var order = repository.save(mapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        order.getReference(),
                        order.getTotalAmount(),
                        order.getPaymentMethod(),
                        customer,
                        purchasedProducts
                        )
        );
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toOrderResponse)
                .toList();
    }

    public OrderResponse findById(Integer orderId) {
        return repository.findById(orderId)
                .map(mapper::toOrderResponse)
                .orElseThrow(() -> new EntityNotFoundException("No order with id " + orderId));
    }
}
