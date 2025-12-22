package com.matfragg.shopping_car.api.products.dto.request;


public record CreateProductRequest(String name, String description, Double price, Integer stock, String category, String imageUrl, boolean active, Boolean available) {
}
