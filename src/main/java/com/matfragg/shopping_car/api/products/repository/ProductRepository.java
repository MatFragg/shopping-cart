package com.matfragg.shopping_car.api.products.repository;

import com.matfragg.shopping_car.api.products.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsBySellerId(Long sellerId);
    List<Product> findByActiveAndAvailableAndSellerIdNot(boolean active, boolean available, Long sellerId);
}
