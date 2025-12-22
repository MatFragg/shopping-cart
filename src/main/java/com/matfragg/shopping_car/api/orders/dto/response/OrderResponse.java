package com.matfragg.shopping_car.api.orders.dto.response;

import com.matfragg.shopping_car.api.orders.model.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        Long buyerId,
        OrderStatus status,
        BigDecimal total,
        LocalDateTime orderDate,
        String shippingAddress,
        List<OrderItemResponse> items,
        Date createdAt,
        Date updatedAt
) {}