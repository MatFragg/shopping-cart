package com.matfragg.shopping_car.api.products.mapper;

import com.matfragg.shopping_car.api.products.dto.request.CreateProductRequest;
import com.matfragg.shopping_car.api.products.dto.request.UpdateProductRequest;
import com.matfragg.shopping_car.api.products.dto.response.ProductResponse;
import com.matfragg.shopping_car.api.products.model.entities.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getImageUrl(),
                product.getSeller().getId(),
                product.getActive(),
                product.getAvailable(),
                product.getCreatedAt()
        );
    }

    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Product toEntity(CreateProductRequest request, Long sellerId) {
        throw new UnsupportedOperationException(
                "Cannot use mapper to create Customer with User relationship. " +
                        "Use CustomerService.createCustomer() instead."
        );
    }

    public void updateEntityFromRequest(Product product, UpdateProductRequest request) {
        if (request.name() != null) {
            product.setName(request.name());
        }
        if (request.description() != null) {
            product.setDescription(request.description());
        }
        if (request.price() != null) {
            product.setPrice(request.price());
        }
        if (request.stock() != null) {
            product.setStock(request.stock());
        }
        if (request.category() != null) {
            product.setCategory(request.category());
        }
        if (request.imageUrl() != null) {
            product.setImageUrl(request.imageUrl());
        }
        if (request.available() != null) {
            product.setAvailable(request.available());
        }
    }
}