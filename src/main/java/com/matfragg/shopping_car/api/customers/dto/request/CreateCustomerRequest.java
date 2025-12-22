package com.matfragg.shopping_car.api.customers.dto.request;

public record CreateCustomerRequest(String firstName, String lastName, String phone, String shippingAddress, Long userId) {
}
