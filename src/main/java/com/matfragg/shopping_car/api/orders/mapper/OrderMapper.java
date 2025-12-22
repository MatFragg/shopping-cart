package com.matfragg.shopping_car.api.orders.mapper;


import com.matfragg.shopping_car.api.orders.dto.response.OrderResponse;
import com.matfragg.shopping_car.api.orders.dto.response.OrderItemResponse;
import com.matfragg.shopping_car.api.orders.model.entities.Order;
import com.matfragg.shopping_car.api.orders.model.entities.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toOrderItemResponse)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getBuyerId(),
                order.getStatus(),
                order.getTotal(),
                order.getOrderDate(),
                order.getShippingAddress(),
                items,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    public OrderItemResponse toOrderItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getSellerId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }

    public List<OrderResponse> toResponseList(List<Order> orders) {
        return orders.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> items) {
        return items.stream()
                .map(this::toOrderItemResponse)
                .collect(Collectors.toList());
    }
}