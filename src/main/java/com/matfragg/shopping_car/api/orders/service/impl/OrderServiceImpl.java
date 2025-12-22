package com.matfragg.shopping_car.api.orders.service.impl;

import com.matfragg.shopping_car.api.customers.dto.response.CustomerResponse;
import com.matfragg.shopping_car.api.customers.service.CustomerService;
import com.matfragg.shopping_car.api.orders.dto.response.OrderItemResponse;
import com.matfragg.shopping_car.api.orders.dto.response.OrderResponse;
import com.matfragg.shopping_car.api.orders.mapper.OrderMapper;
import com.matfragg.shopping_car.api.orders.model.entities.Order;
import com.matfragg.shopping_car.api.orders.model.entities.OrderItem;
import com.matfragg.shopping_car.api.orders.model.enums.OrderStatus;
import com.matfragg.shopping_car.api.orders.repository.OrderItemRepository;
import com.matfragg.shopping_car.api.orders.repository.OrderRepository;
import com.matfragg.shopping_car.api.orders.service.OrderService;
import com.matfragg.shopping_car.api.products.dto.response.ProductResponse;
import com.matfragg.shopping_car.api.products.service.ProductService;
import com.matfragg.shopping_car.api.shared.exceptions.BusinessException;
import com.matfragg.shopping_car.api.shared.exceptions.ResourceNotFoundException;
import com.matfragg.shopping_car.api.shoppingcart.dto.response.CartItemResponse;
import com.matfragg.shopping_car.api.shoppingcart.dto.response.CartResponse;
import com.matfragg.shopping_car.api.shoppingcart.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            CartService cartService,
                            ProductService productService,
                            CustomerService customerService,
                            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.productService = productService;
        this.customerService = customerService;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderResponse checkout(Long buyerId) {
        CartResponse cart = cartService.getActiveCart(buyerId);

        if (cart.items().isEmpty())
            throw new BusinessException("Cart is empty. Add products before checkout.");

        CustomerResponse buyer = customerService.findCustomerById(buyerId);

        for (CartItemResponse cartItem : cart.items()) {
            ProductResponse product = productService.getProductById(cartItem.productId());

            if (product.sellerId().equals(buyerId))
                throw new BusinessException(
                        "Cannot purchase your own product: " + product.name());

            if (product.stock() < cartItem.quantity())
                throw new BusinessException(
                        "Insufficient stock for product: " + product.name() +
                                ". Available: " + product.stock());

            productService.decreaseStock(cartItem.productId(), cartItem.quantity());
        }

        Order order = new Order(
                generateOrderNumber(),
                buyerId,
                OrderStatus.PENDING,
                cart.total(),
                LocalDateTime.now(),
                buyer.shippingAddress()
        );

        for (CartItemResponse cartItem : cart.items()) {
            OrderItem orderItem = new OrderItem(
                    order,
                    cartItem.productId(),
                    cartItem.sellerId(),
                    cartItem.productName(),
                    cartItem.quantity(),
                    cartItem.unitPrice(),
                    cartItem.subtotal()
            );

            order.getItems().add(orderItem);
        }

        Order savedOrder = orderRepository.save(order);

        cartService.markAsConverted(cart.id());

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with number: " + orderNumber));

        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getMyPurchases(Long buyerId) {
        customerService.findCustomerById(buyerId);

        List<Order> orders = orderRepository.findByBuyerId(buyerId);
        return orderMapper.toResponseList(orders);
    }

    @Override
    public List<OrderResponse> getMyPurchasesByStatus(Long buyerId, OrderStatus status) {
        customerService.findCustomerById(buyerId);

        List<Order> orders = orderRepository.findByBuyerIdAndStatus(buyerId, status);
        return orderMapper.toResponseList(orders);
    }

    @Override
    public List<OrderItemResponse> getMySales(Long sellerId) {
        customerService.findCustomerById(sellerId);

        List<OrderItem> soldItems = orderItemRepository.findBySellerId(sellerId);
        return orderMapper.toOrderItemResponseList(soldItems);
    }

    @Override
    public OrderResponse updateOrderStatus(String orderNumber, OrderStatus newStatus) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with number: " + orderNumber));

        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);

        return orderMapper.toResponse(updated);
    }

    @Override
    public OrderResponse cancelOrder(Long buyerId, String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with number: " + orderNumber));

        if (!order.getBuyerId().equals(buyerId))
            throw new BusinessException(
                    "You don't have permission to cancel this order");


        if (order.getStatus() != OrderStatus.PENDING)
            throw new BusinessException(
                    "Only pending orders can be cancelled. Current status: " + order.getStatus());


        for (OrderItem item : order.getItems())
            productService.increaseStock(item.getProductId(), item.getQuantity());


        order.setStatus(OrderStatus.CANCELLED);
        Order cancelled = orderRepository.save(order);

        return orderMapper.toResponse(cancelled);
    }

    private String generateOrderNumber() {
        LocalDateTime now = LocalDateTime.now();
        long count = orderRepository.count() + 1;
        return String.format("ORD-%d%02d%02d-%06d",
                now.getYear(), now.getMonthValue(), now.getDayOfMonth(), count);
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        switch (current) {
            case PENDING:
                if (next != OrderStatus.PAID && next != OrderStatus.CANCELLED)
                    throw new BusinessException(
                            "Invalid status transition from PENDING to " + next);

                break;
            case PAID:
                if (next != OrderStatus.PROCESSING)
                    throw new BusinessException(
                            "Invalid status transition from PAID to " + next);

                break;
            case PROCESSING:
                if (next != OrderStatus.SHIPPED)
                    throw new BusinessException(
                            "Invalid status transition from PROCESSING to " + next);

                break;
            case SHIPPED:
                if (next != OrderStatus.DELIVERED)
                    throw new BusinessException(
                            "Invalid status transition from SHIPPED to " + next);

                break;
            case DELIVERED:
            case CANCELLED:
                throw new BusinessException(
                        "Cannot change status of a " + current + " order");
        }
    }


}
