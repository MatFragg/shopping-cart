package com.matfragg.shopping_car.api.authentication.dto.response;

public record CustomerInfo(
        Long customerId,
        String firstName,
        String lastName,
        String phone,
        String shippingAddress
) {}