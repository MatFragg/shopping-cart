package com.matfragg.shopping_car.api.shoppingcart.dto.response;

import com.matfragg.shopping_car.api.shoppingcart.model.enums.CartStatus;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record CartResponse(
        Long id,
        Long customerId,
        CartStatus status,
        BigDecimal total,
        List<CartItemResponse> items,
        Date createdAt,
        Date updatedAt
) {}