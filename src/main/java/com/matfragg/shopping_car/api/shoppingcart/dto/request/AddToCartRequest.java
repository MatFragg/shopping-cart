package com.matfragg.shopping_car.api.shoppingcart.dto.request;

public record AddToCartRequest(
        Long productId,
        Integer quantity
) {
}
