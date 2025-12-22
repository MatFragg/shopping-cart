package com.matfragg.shopping_car.api.orders.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record SalesReportResponse(
        Long sellerId,
        Long totalItemsSold,
        BigDecimal totalRevenue,
        List<OrderItemResponse> recentSales
) {}