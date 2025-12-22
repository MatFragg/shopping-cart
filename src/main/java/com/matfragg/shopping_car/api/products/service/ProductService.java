package com.matfragg.shopping_car.api.products.service;

import com.matfragg.shopping_car.api.products.dto.request.CreateProductRequest;
import com.matfragg.shopping_car.api.products.dto.request.UpdateProductRequest;
import com.matfragg.shopping_car.api.products.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(Long sellerId, CreateProductRequest request);
    ProductResponse updateProduct(Long productId, UpdateProductRequest request);
    ProductResponse deleteProduct(Long productId);
    ProductResponse getProductById(Long productId);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getProductsBySellerId(Long sellerId);
    List<ProductResponse> getAvailableProducts(Long excludeSellerId);

}
