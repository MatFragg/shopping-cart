package com.matfragg.shopping_car.api.products.dto.request;

public record UpdateProductRequest(String name, String description, Double price, Integer stock, String category, String imageUrl, Boolean active, Boolean available) {
}
