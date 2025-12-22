package com.matfragg.shopping_car.api.customers.mapper;

import com.matfragg.shopping_car.api.customers.dto.request.CreateCustomerRequest;
import com.matfragg.shopping_car.api.customers.dto.response.CustomerResponse;
import com.matfragg.shopping_car.api.customers.model.entities.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {

    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhone(),
                customer.getShippingAddress(),
                customer.getUser().getId()
        );
    }

    public List<CustomerResponse> toResponseList(List<Customer> customers) {
        return customers.stream()
                .map(this::toResponse)
                .toList();
    }

    public Customer toEntity(CreateCustomerRequest request) {
        throw new UnsupportedOperationException(
                "Cannot use mapper to create Customer with User relationship. " +
                        "Use CustomerService.createCustomer() instead."
        );
    }
}
