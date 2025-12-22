package com.matfragg.shopping_car.api.orders.service;

import com.matfragg.shopping_car.api.orders.dto.response.OrderItemResponse;
import com.matfragg.shopping_car.api.orders.dto.response.OrderResponse;
import com.matfragg.shopping_car.api.orders.model.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse checkout(Long buyerId);
    OrderResponse getOrderByNumber(String orderNumber);
    List<OrderResponse> getMyPurchases(Long buyerId);
    List<OrderResponse> getMyPurchasesByStatus(Long buyerId, OrderStatus status);
    List<OrderItemResponse> getMySales(Long sellerId);
    OrderResponse updateOrderStatus(String orderNumber, OrderStatus newStatus);
    OrderResponse cancelOrder(Long buyerId, String orderNumber);
}