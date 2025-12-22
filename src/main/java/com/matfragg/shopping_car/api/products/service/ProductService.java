package com.matfragg.shopping_car.api.products.service;

import com.matfragg.shopping_car.api.products.dto.request.CreateProductRequest;
import com.matfragg.shopping_car.api.products.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(Long sellerId, CreateProductRequest request);

    List<ProductResponse> findBySellerId(Long sellerId);

    List<ProductResponse> findAvailableProducts(Long excludeSellerId);

}
