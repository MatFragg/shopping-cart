package com.matfragg.shopping_car.api.customers.model.entities;

import com.matfragg.shopping_car.api.authentication.model.entities.User;
import com.matfragg.shopping_car.api.shared.model.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer extends BaseModel {
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_customer_user")
    )
    private User user;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String phone, String shippingAddress, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.shippingAddress = shippingAddress;
        this.user = user;
    }

    @Transient
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public Customer updateDetails(String firstName, String lastName, String phone, String shippingAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.shippingAddress = shippingAddress;
        return this;
    }
}
