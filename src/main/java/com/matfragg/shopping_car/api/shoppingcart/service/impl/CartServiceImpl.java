package com.matfragg.shopping_car.api.shoppingcart.service.impl;

import com.matfragg.shopping_car.api.customers.dto.response.CustomerResponse;
import com.matfragg.shopping_car.api.customers.service.CustomerService;
import com.matfragg.shopping_car.api.products.dto.response.ProductResponse;
import com.matfragg.shopping_car.api.products.service.ProductService;
import com.matfragg.shopping_car.api.shared.exceptions.BusinessException;
import com.matfragg.shopping_car.api.shoppingcart.dto.request.AddToCartRequest;
import com.matfragg.shopping_car.api.shoppingcart.dto.response.CartResponse;
import com.matfragg.shopping_car.api.shoppingcart.mapper.CartMapper;
import com.matfragg.shopping_car.api.shoppingcart.model.entities.Cart;
import com.matfragg.shopping_car.api.shoppingcart.model.entities.CartItem;
import com.matfragg.shopping_car.api.shoppingcart.model.enums.CartStatus;
import com.matfragg.shopping_car.api.shoppingcart.repository.CartItemRepository;
import com.matfragg.shopping_car.api.shoppingcart.repository.CartRepository;
import com.matfragg.shopping_car.api.shoppingcart.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CustomerService customerService;
    private final CartMapper cartMapper;

    @Override
    public CartResponse addItem(Long buyerId, AddToCartRequest request) {
        CustomerResponse buyer = customerService.findCustomerById(buyerId);

        ProductResponse product = productService.getProductById(request.productId());

        if (product.sellerId().equals(buyerId))
            throw new BusinessException(
                    "Cannot add your own product to cart. You cannot buy products you are selling.");


        if (!product.available())
            throw new BusinessException("Product is not available for purchase");


        if (product.stock() < request.quantity())
            throw new BusinessException(
                    "Insufficient stock. Available: " + product.stock());

        Cart cart = cartRepository
                .findByCustomerIdAndStatus(buyerId, CartStatus.ACTIVE)
                .orElseGet(() -> createNewCart(buyerId));

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.productId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + request.quantity();

            if (product.stock() < newQuantity)
                throw new BusinessException(
                        "Insufficient stock. Available: " + product.stock());


            existingItem.setQuantity(newQuantity);
            existingItem.calculateSubtotal();
        } else {
            CartItem newItem = new CartItem(
                    request.productId(),
                    product.sellerId(),
                    product.name(),
                    request.quantity(),
                    BigDecimal.valueOf(product.price())
            );

            cart.addItem(newItem);
        }

        cart.recalculateTotal();
        Cart saved = cartRepository.save(cart);


        return cartMapper.toResponse(saved);
    }

    private Cart createNewCart(Long customerId) {
        Cart cart = new Cart();
        cart.setCustomerId(customerId);
        cart.setStatus(CartStatus.ACTIVE);
        cart.setTotal(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }
}