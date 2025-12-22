package com.matfragg.shopping_car.api.shoppingcart.service;

import com.matfragg.shopping_car.api.shoppingcart.dto.request.AddToCartRequest;
import com.matfragg.shopping_car.api.shoppingcart.dto.response.CartResponse;
import com.matfragg.shopping_car.api.shoppingcart.model.entities.Cart;

public interface CartService {
    CartResponse addItem(Long buyerId, AddToCartRequest request);
}
