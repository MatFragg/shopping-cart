package com.matfragg.shopping_car.api.products.repository;

import com.matfragg.shopping_car.api.products.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.seller.id = :sellerId")
    List<Product> findProductsBySellerId(@Param("sellerId") Long sellerId);

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.available = true")
    List<Product> findAvailableProducts();

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.available = true AND p.seller.id <> :excludeSellerId")
    List<Product> findAvailableProductsExcludingSeller(@Param("excludeSellerId") Long excludeSellerId);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.active = true")
    List<Product> findByName(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.active = true")
    List<Product> findByCategory(@Param("category") String category);

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.seller.id <> :excludeSellerId AND p.active = true")
    List<Product> findByCategoryAndSellerIdNot(@Param("category") String category, @Param("excludeSellerId") Long excludeSellerId);
}
