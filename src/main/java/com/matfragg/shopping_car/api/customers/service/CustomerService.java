package com.matfragg.shopping_car.api.customers.service;

import com.matfragg.shopping_car.api.customers.dto.request.CreateCustomerRequest;
import com.matfragg.shopping_car.api.customers.dto.request.UpdateCustomerRequest;
import com.matfragg.shopping_car.api.customers.dto.response.CustomerResponse;

public interface CustomerService {
    CustomerResponse createCustomer(CreateCustomerRequest request);
    CustomerResponse findByUserId(Long userId);
    CustomerResponse updateCustomer(Long userId, UpdateCustomerRequest request);
}
