package com.matfragg.shopping_car.api.customers.dto.response;

public record CustomerResponse(Long id,String firstName, String lastName, String phone, String shippingAddress, Long userId) {
}
