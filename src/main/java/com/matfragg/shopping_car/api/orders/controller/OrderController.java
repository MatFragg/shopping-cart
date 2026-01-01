package com.matfragg.shopping_car.api.orders.controller;

import com.matfragg.shopping_car.api.orders.dto.response.OrderItemResponse;
import com.matfragg.shopping_car.api.orders.dto.response.OrderResponse;
import com.matfragg.shopping_car.api.orders.model.enums.OrderStatus;
import com.matfragg.shopping_car.api.orders.service.OrderService;
import com.matfragg.shopping_car.api.shared.dto.ApiResponse;
import com.matfragg.shopping_car.api.shared.security.annotation.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management endpoints")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @Operation(summary = "Checkout", description = "Creates an order from the current cart")
    public ResponseEntity<ApiResponse<OrderResponse>> checkout(@Parameter(hidden = true) @CurrentUserId Long userId) {
        OrderResponse order = orderService.checkout(userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", order));
    }

    @GetMapping("/{orderNumber}")
    @Operation(summary = "Get order by number", description = "Returns order details")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable String orderNumber) {
        OrderResponse order = orderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.success( order));
    }

    @GetMapping("/purchases")
    @Operation(summary = "Get my purchases", description = "Returns all orders made by the authenticated user")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyPurchases(
            @Parameter(hidden = true) @CurrentUserId Long userId,
            @Parameter(description = "Filter by order status")
            @RequestParam(required = false) OrderStatus status) {

        List<OrderResponse> orders;
        if (status != null) {
            orders = orderService.getMyPurchasesByStatus(userId, status);
        } else {
            orders = orderService.getMyPurchases(userId);
        }

        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/sales")
    @Operation(summary = "Get my sales", description = "Returns all items sold by the authenticated user")
    public ResponseEntity<ApiResponse<List<OrderItemResponse>>> getMySales(@Parameter(hidden = true) @CurrentUserId Long userId) {

        List<OrderItemResponse> sales = orderService.getMySales(userId);

        return ResponseEntity.ok(ApiResponse.success(sales));
    }

    @PutMapping("/{orderNumber}/cancel")
    @Operation(summary = "Cancel order", description = "Cancels a pending order")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable String orderNumber, @Parameter(hidden = true) @CurrentUserId Long userId) {

        OrderResponse order = orderService.cancelOrder(userId, orderNumber);

        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", order));
    }

    @PatchMapping("/{orderNumber}/status")
    @Operation(summary = "Update order status", description = "Updates order status (admin only)")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable String orderNumber,
            @RequestParam OrderStatus status) {

        OrderResponse order = orderService.updateOrderStatus(orderNumber, status);
        return ResponseEntity.ok(ApiResponse.success("Order status updated", order));
    }
}