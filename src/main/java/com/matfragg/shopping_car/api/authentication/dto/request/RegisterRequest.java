package com.matfragg.shopping_car.api.authentication.dto.request;

public record RegisterRequest(String username, String password, String firstName, String lastName, String email, String phone, String shippingAddress) {
}
