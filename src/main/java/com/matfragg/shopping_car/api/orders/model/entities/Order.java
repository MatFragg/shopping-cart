package com.matfragg.shopping_car.api.orders.model.entities;

import com.matfragg.shopping_car.api.orders.model.enums.OrderStatus;
import com.matfragg.shopping_car.api.shared.model.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseModel {

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    public Order(String orderNumber, Long buyerId, OrderStatus orderStatus, BigDecimal total, LocalDateTime orderDate, String shippingAddress) {
        this.orderNumber = orderNumber;
        this.buyerId = buyerId;
        this.status = orderStatus;
        this.total = total;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
    }
}