package com.matfragg.shopping_car.api.shoppingcart.controller;

import com.matfragg.shopping_car.api.shared.dto.ApiResponse;
import com.matfragg.shopping_car.api.shoppingcart.dto.request.AddToCartRequest;
import com.matfragg.shopping_car.api.shoppingcart.dto.response.CartResponse;
import com.matfragg.shopping_car.api.shoppingcart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Shopping Cart", description = "Endpoints for managing the shopping cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Add item to buyer's shopping cart
     *
     * @param buyerId ID of the buyer
     * @param request Details of the item to add
     * @return ApiResponse with CartResponse
     */
    @PostMapping("/{buyerId}/items")
    @Operation(summary = "Add Item to Cart",description = "Adds an item to the buyer's shopping cart.")
    public ResponseEntity<ApiResponse<CartResponse>> addItemToCart(@PathVariable Long buyerId, @Valid @RequestBody AddToCartRequest request) {
        CartResponse cartItemResponse = cartService.addItem(buyerId, request);
        return ResponseEntity.ok(ApiResponse.success("Item added to cart successfully", cartItemResponse));
    }
}
