package com.matfragg.shopping_car.api.shoppingcart.service;

import com.matfragg.shopping_car.api.shoppingcart.dto.request.AddToCartRequest;
import com.matfragg.shopping_car.api.shoppingcart.dto.request.UpdateCartItemRequest;
import com.matfragg.shopping_car.api.shoppingcart.dto.response.CartResponse;
import com.matfragg.shopping_car.api.shoppingcart.model.entities.Cart;

public interface CartService {
    CartResponse addItem(Long userId, AddToCartRequest request);
    CartResponse getActiveCart(Long userId);
    CartResponse updateItemQuantity(Long userId, Long productId, UpdateCartItemRequest request);
    void removeItem(Long userId, Long productId);
    void clearCart(Long userId);
    void markAsConverted(Long cartId);
}
