package com.matfragg.shopping_car.api.shoppingcart.model.entities;

import com.matfragg.shopping_car.api.customers.model.entities.Customer;
import com.matfragg.shopping_car.api.shared.exceptions.BusinessException;
import com.matfragg.shopping_car.api.shared.model.BaseModel;
import com.matfragg.shopping_car.api.shoppingcart.model.enums.CartStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_cart_customer"))
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CartStatus status = CartStatus.ACTIVE;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
        recalculateTotal();
    }

    public void updateItem(Long productId,Integer newQuantity, BigDecimal currentPrice) {
        if (newQuantity <= 0) {
            this.items.removeIf(item -> item.getProduct().getId().equals(productId));
            recalculateTotal();
            return;
        }
        this.items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.updateItemQuantity(newQuantity, currentPrice);
                    recalculateTotal();
                });
    }

    public void removeItemByProductId(Long productId) {
        boolean removed = items.removeIf(item -> item.getProduct().getId().equals(productId));
        if (removed)
            recalculateTotal();
        else
            throw new BusinessException("Product not found in cart");
    }

    public void clear() {
        this.items.clear();
        this.total = BigDecimal.ZERO;
    }

    public void recalculateTotal() {
        this.total = items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}