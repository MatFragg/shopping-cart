package com.matfragg.shopping_car.api.shoppingcart.service.impl;

import com.matfragg.shopping_car.api.customers.dto.response.CustomerResponse;
import com.matfragg.shopping_car.api.customers.model.entities.Customer;
import com.matfragg.shopping_car.api.customers.repository.CustomerRepository;
import com.matfragg.shopping_car.api.customers.service.CustomerService;
import com.matfragg.shopping_car.api.products.dto.response.ProductResponse;
import com.matfragg.shopping_car.api.products.repository.ProductRepository;
import com.matfragg.shopping_car.api.products.service.ProductService;
import com.matfragg.shopping_car.api.shared.exceptions.BusinessException;
import com.matfragg.shopping_car.api.shared.exceptions.ResourceNotFoundException;
import com.matfragg.shopping_car.api.shoppingcart.dto.request.AddToCartRequest;
import com.matfragg.shopping_car.api.shoppingcart.dto.request.UpdateCartItemRequest;
import com.matfragg.shopping_car.api.shoppingcart.dto.response.CartResponse;
import com.matfragg.shopping_car.api.shoppingcart.mapper.CartMapper;
import com.matfragg.shopping_car.api.shoppingcart.model.entities.Cart;
import com.matfragg.shopping_car.api.shoppingcart.model.entities.CartItem;
import com.matfragg.shopping_car.api.shoppingcart.model.enums.CartStatus;
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
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private final ProductService productService;
    private final CustomerService customerService;
    private final CartMapper cartMapper;

    @Override
    public CartResponse addItem(Long userId, AddToCartRequest request) {
        var customerEntity = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        ProductResponse productDto = productService.getProductById(request.productId());
        var productEntity = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        var sellerEntity = customerRepository.findById(productDto.sellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        if (productDto.sellerId().equals(customerEntity.getId()))
            throw new BusinessException("Cannot add your own product to cart.");

        if (!productDto.available())
            throw new BusinessException("Product is not available for purchase");

        Cart cart = cartRepository
                .findByCustomerIdAndStatus(customerEntity.getId(), CartStatus.ACTIVE)
                .orElseGet(() -> createNewCart(customerEntity));

        // 4. Lógica de Items
        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.productId()))
                .findFirst()
                .ifPresentOrElse(
                        existingItem -> {
                            int totalQuantity = existingItem.getQuantity() + request.quantity();
                            if (totalQuantity > productDto.stock()) {
                                throw new BusinessException("Insufficient stock. Available: " + productDto.stock());
                            }
                            existingItem.updateItemQuantity(totalQuantity, BigDecimal.valueOf(productDto.price()));
                        },
                        () -> {
                            if (request.quantity() > productDto.stock()) {
                                throw new BusinessException("Insufficient stock.");
                            }
                            // AHORA PASAMOS LAS ENTIDADES COMPLETAS
                            CartItem newItem = new CartItem(
                                    productEntity,
                                    sellerEntity,
                                    productDto.name(),
                                    request.quantity(),
                                    BigDecimal.valueOf(productDto.price())
                            );
                            cart.addItem(newItem);
                        }
                );

        cart.recalculateTotal();
        return cartMapper.toResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse getActiveCart(Long userId) {
        Long customerId = customerService.findByUserId(userId).id();
        Cart cart = cartRepository
                .findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException("No active cart found for customer with ID: " + customerId));

        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse updateItemQuantity(Long userId, Long productId, UpdateCartItemRequest request) {
        Long customerId = customerService.findByUserId(userId).id();

        Cart cart = cartRepository
                .findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active cart found for this customer"));

        ProductResponse product = productService.getProductById(productId);

        if (request.quantity() > product.stock())
            throw new BusinessException("Insufficient stock. Available: " + product.stock());


        boolean itemExists = cart.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(productId));

        if (!itemExists)
            throw new ResourceNotFoundException("Product with ID " + productId + " is not in your cart");


        cart.updateItem(productId, request.quantity(), BigDecimal.valueOf(product.price()));

        cart.recalculateTotal();
        Cart saved = cartRepository.save(cart);
        return cartMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void removeItem(Long userId, Long productId) {
        Long customerId = customerService.findByUserId(userId).id();

        Cart cart = cartRepository
                .findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.removeItemByProductId(productId);

        cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long customerId) {
        Cart cart = cartRepository
                .findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for customer ID: " + customerId));

        cart.clear();

        cartRepository.save(cart);
    }

    @Override
    public void markAsConverted(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.setStatus(CartStatus.CONVERTED);
        cartRepository.save(cart);
    }

    private Cart createNewCart(Customer customer) {
        Cart cart = new Cart();
        cart.setCustomer(customer); // Ya no es setCustomerId
        cart.setStatus(CartStatus.ACTIVE);
        cart.setTotal(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }


}