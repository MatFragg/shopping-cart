package com.matfragg.shopping_car.api.customers.dto.request;

public record UpdateCustomerRequest(
        String firstName,
        String lastName,
        String phone,
        String shippingAddress
) {
}
