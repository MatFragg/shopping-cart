package com.matfragg.shopping_car.api.orders.repository;

import com.matfragg.shopping_car.api.orders.model.entities.Order;
import com.matfragg.shopping_car.api.orders.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByBuyerId(Long buyerId);

    List<Order> findByBuyerIdAndStatus(Long buyerId, OrderStatus status);

    List<Order> findByStatus(OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findByOrderDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT o FROM Order o WHERE o.buyerId = :buyerId " +
            "AND o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findByBuyerIdAndOrderDateBetween(
            @Param("buyerId") Long buyerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    long countByBuyerId(Long buyerId);

    boolean existsByOrderNumber(String orderNumber);

    @Query("SELECT SUM(o.total) FROM Order o WHERE o.buyerId = :buyerId " +
            "AND o.status NOT IN ('CANCELLED')")
    Double getTotalPurchasesByBuyer(@Param("buyerId") Long buyerId);
}