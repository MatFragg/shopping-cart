package com.matfragg.shopping_car.api.shoppingcart.model.entities;

import com.matfragg.shopping_car.api.customers.model.entities.Customer;
import com.matfragg.shopping_car.api.products.model.entities.Product;
import com.matfragg.shopping_car.api.shared.model.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cart_items")
public class CartItem extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_item_product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false, foreignKey = @ForeignKey(name = "fk_item_seller"))
    private Customer seller;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    public CartItem() {
    }

    public CartItem(Product product, Customer seller, String productName, Integer quantity, BigDecimal unitPrice) {
        this.product = product;
        this.seller = seller;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    public void updateItemQuantity(Integer newQuantity, BigDecimal currentPrice) {
        this.unitPrice = currentPrice;
        this.quantity = newQuantity;
        calculateSubtotal();
    }

    public void calculateSubtotal() {
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}