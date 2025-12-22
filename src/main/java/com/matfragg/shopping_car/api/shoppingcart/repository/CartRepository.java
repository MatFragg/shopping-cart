package com.matfragg.shopping_car.api.shoppingcart.repository;

import com.matfragg.shopping_car.api.shoppingcart.model.entities.Cart;
import com.matfragg.shopping_car.api.shoppingcart.model.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomerIdAndStatus(Long customerId, CartStatus status);

    List<Cart> findByCustomerId(Long customerId);

    List<Cart> findByStatus(CartStatus status);

    @Query("SELECT c FROM Cart c WHERE c.status = 'ACTIVE' " +
            "AND c.updatedAt < :cutoffDate")
    List<Cart> findAbandonedCarts(@Param("cutoffDate") LocalDateTime cutoffDate);

    boolean existsByCustomerIdAndStatus(Long customerId, CartStatus status);

    @Query("SELECT COUNT(ci) FROM Cart c JOIN c.items ci " +
            "WHERE c.customer.id = :customerId AND c.status = 'ACTIVE'")
    long countActiveCartItems(@Param("customerId") Long customerId);
}