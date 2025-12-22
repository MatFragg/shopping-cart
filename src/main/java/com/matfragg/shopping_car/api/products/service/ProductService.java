package com.matfragg.shopping_car.api.products.service;

import com.matfragg.shopping_car.api.products.dto.request.CreateProductRequest;
import com.matfragg.shopping_car.api.products.dto.request.UpdateProductRequest;
import com.matfragg.shopping_car.api.products.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(Long sellerId, CreateProductRequest request);
    ProductResponse updateProduct(Long sellerId, Long productId, UpdateProductRequest request);
    void deleteProduct(Long sellerId, Long productId);
    ProductResponse getProductById(Long productId);
    List<ProductResponse> getAllAvailableProducts();
    List<ProductResponse> getAvailableProductsExcludingSeller(Long excludeSellerId);
    List<ProductResponse> getProductsBySellerId(Long sellerId);
    List<ProductResponse> getMyProducts(Long sellerId);
    List<ProductResponse> searchProducts(String searchTerm);
    List<ProductResponse> getProductsByCategory(String category, Long excludeSellerId);
    void decreaseStock(Long productId, Integer quantity);
    void increaseStock(Long productId, Integer quantity);

}
