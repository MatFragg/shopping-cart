package com.matfragg.shopping_car.api.customers.service;

import com.matfragg.shopping_car.api.customers.dto.request.CreateCustomerRequest;
import com.matfragg.shopping_car.api.customers.dto.request.UpdateCustomerRequest;
import com.matfragg.shopping_car.api.customers.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CreateCustomerRequest request);
    CustomerResponse findByUserId(Long userId);
    CustomerResponse findCustomerById(Long customerId);
    List<CustomerResponse> findAllCustomers();
    CustomerResponse updateCustomer(Long userId, UpdateCustomerRequest request);
}
