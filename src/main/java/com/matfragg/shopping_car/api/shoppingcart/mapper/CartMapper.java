package com.matfragg.shopping_car.api.shoppingcart.mapper;

import com.matfragg.shopping_car.api.shoppingcart.dto.response.CartItemResponse;
import com.matfragg.shopping_car.api.shoppingcart.dto.response.CartResponse;
import com.matfragg.shopping_car.api.shoppingcart.model.entities.Cart;
import com.matfragg.shopping_car.api.shoppingcart.model.entities.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::toCartItemResponse)
                .collect(Collectors.toList());

        return new CartResponse(
                cart.getId(),
                cart.getCustomerId(),
                cart.getStatus(),
                cart.getTotal(),
                items,
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }

    public CartItemResponse toCartItemResponse(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getProductId(),
                item.getSellerId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }

    public List<CartItemResponse> toCartItemResponseList(List<CartItem> items) {
        return items.stream()
                .map(this::toCartItemResponse)
                .collect(Collectors.toList());
    }
}