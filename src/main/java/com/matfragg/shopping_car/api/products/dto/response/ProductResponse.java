package com.matfragg.shopping_car.api.products.dto.response;

import java.math.BigDecimal;
import java.util.Date;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price,
        Integer stock,
        String category,
        String imageUrl,
        Long sellerId,
        Boolean active,
        Boolean available,
        Date createdAt
) {}