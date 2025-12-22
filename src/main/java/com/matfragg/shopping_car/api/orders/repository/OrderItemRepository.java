package com.matfragg.shopping_car.api.orders.repository;

import com.matfragg.shopping_car.api.orders.model.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    List<OrderItem> findBySellerId(Long sellerId);

    List<OrderItem> findByProductId(Long productId);

    long countBySellerId(Long sellerId);

    @Query("SELECT SUM(oi.subtotal) FROM OrderItem oi " +
            "WHERE oi.sellerId = :sellerId " +
            "AND oi.order.status NOT IN ('CANCELLED')")
    Double getTotalSalesBySeller(@Param("sellerId") Long sellerId);

    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi " +
            "WHERE oi.productId = :productId " +
            "AND oi.order.status NOT IN ('CANCELLED')")
    Long getTotalQuantitySoldByProduct(@Param("productId") Long productId);

    boolean existsByProductId(Long productId);

    @Query("SELECT oi FROM OrderItem oi " +
            "WHERE oi.sellerId = :sellerId " +
            "AND oi.order.status = :status")
    List<OrderItem> findBySellerIdAndOrderStatus(
            @Param("sellerId") Long sellerId,
            @Param("status") com.matfragg.shopping_car.api.orders.model.enums.OrderStatus status
    );
}