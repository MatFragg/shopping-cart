package com.matfragg.shopping_car.api.products.model.entities;

import com.matfragg.shopping_car.api.customers.model.entities.Customer;
import com.matfragg.shopping_car.api.shared.model.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseModel {

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 100)
    private String category;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_customer"))
    private Customer seller;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Boolean available = true;

    public Product() {
    }

    public Product(String name, String description, Double price, Integer stock, String category, String imageUrl, Long sellerId, Boolean active, Boolean available) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.imageUrl = imageUrl;
        this.active = active;
        this.available = available;
    }
}
