package com.matfragg.shopping_car.api.shoppingcart.repository;

import com.matfragg.shopping_car.api.shoppingcart.model.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    List<CartItem> findByCartId(Long cartId);

    List<CartItem> findByProductId(Long productId);

    boolean existsByProductId(Long productId);

    @Query("SELECT COUNT(DISTINCT ci.cart.id) FROM CartItem ci WHERE ci.productId = :productId")
    long countCartsContainingProduct(@Param("productId") Long productId);
}
