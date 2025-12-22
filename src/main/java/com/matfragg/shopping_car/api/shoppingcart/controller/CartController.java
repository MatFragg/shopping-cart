package com.matfragg.shopping_car.api.shoppingcart.controller;

import com.matfragg.shopping_car.api.shared.dto.ApiResponse;
import com.matfragg.shopping_car.api.shared.security.annotation.CurrentUserId;
import com.matfragg.shopping_car.api.shoppingcart.dto.request.AddToCartRequest;
import com.matfragg.shopping_car.api.shoppingcart.dto.request.UpdateCartItemRequest;
import com.matfragg.shopping_car.api.shoppingcart.dto.response.CartResponse;
import com.matfragg.shopping_car.api.shoppingcart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
     * @param userId  ID of the authenticated user
     * @param request Details of the item to add
     * @return ApiResponse with CartResponse
     */
    @PostMapping("/items")
    @Operation(summary = "Add Item to Cart", description = "Adds an item to the authenticated user's cart.")
    public ResponseEntity<ApiResponse<CartResponse>> addItemToCart(@Parameter(hidden = true) @CurrentUserId Long userId,
            @Valid @RequestBody AddToCartRequest request) {

        CartResponse cart = cartService.addItem(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item added to cart", cart));
    }

    /**
     * Get the active shopping cart for the authenticated user
     * @param userId ID of the authenticated user
     * @return ApiResponse with CartResponse
     */
    @GetMapping
    @Operation(summary = "Get Active Cart", description = "Retrieves the active shopping cart for the authenticated user.")
    public ResponseEntity<ApiResponse<CartResponse>> getActiveCart(@Parameter(hidden = true) @CurrentUserId Long userId) {
        CartResponse cart = cartService.getActiveCart(userId);

        return ResponseEntity.ok(ApiResponse.success("Active cart retrieved", cart));
    }

    /**
     * Update the quantity of a specific item in the cart
     *
     * @param userId    ID of the authenticated user
     * @param productId ID of the product to update
     * @param request   Details of the quantity update
     * @return ApiResponse with CartResponse
     */
    @PutMapping("/items/{productId}")
    @Operation(summary = "Update Cart Item Quantity", description = "Updates the quantity of a specific item in the cart.")
    public ResponseEntity<ApiResponse<CartResponse>> updateItemQuantity(@Parameter(hidden = true) @CurrentUserId Long userId, @PathVariable Long productId,@Valid @RequestBody UpdateCartItemRequest request) {
        CartResponse cart = cartService.updateItemQuantity(userId, productId, request);
        return ResponseEntity.ok(ApiResponse.success("Cart item quantity updated", cart));
    }

    /**
     * Remove an item from the cart
     *
     * @param userId    ID of the authenticated user
     * @param productId ID of the product to remove
     * @return ApiResponse with no content
     */
    @DeleteMapping("/items/{productId}")
    @Operation(summary = "Remove Item from Cart", description = "Removes an item from the cart.")
    public ResponseEntity<ApiResponse<Void>> removeItemFromCart(@Parameter(hidden = true) @CurrentUserId Long userId, @PathVariable Long productId) {
        cartService.removeItem(userId, productId);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart", null));
    }

    /**
     * Clear all items from the cart
     *
     * @param userId ID of the authenticated user
     * @return ApiResponse with no content
     */
    @DeleteMapping("/clear")
    @Operation(summary = "Clear Cart", description = "Clears all items from the cart.")
    public ResponseEntity<ApiResponse<Void>> clearCart(@Parameter(hidden = true) @CurrentUserId Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success("Cart cleared", null));
    }
}
