package com.matfragg.shopping_car.api.customers.service.impl;

import com.matfragg.shopping_car.api.authentication.model.entities.User;
import com.matfragg.shopping_car.api.authentication.repository.UserRepository;
import com.matfragg.shopping_car.api.authentication.service.UserService;
import com.matfragg.shopping_car.api.customers.dto.request.CreateCustomerRequest;
import com.matfragg.shopping_car.api.customers.dto.request.UpdateCustomerRequest;
import com.matfragg.shopping_car.api.customers.dto.response.CustomerResponse;
import com.matfragg.shopping_car.api.customers.mapper.CustomerMapper;
import com.matfragg.shopping_car.api.customers.model.entities.Customer;
import com.matfragg.shopping_car.api.customers.repository.CustomerRepository;
import com.matfragg.shopping_car.api.customers.service.CustomerService;
import com.matfragg.shopping_car.api.shared.exceptions.BusinessException;
import com.matfragg.shopping_car.api.shared.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;
    private final UserService userService;

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.userId()));

        if (customerRepository.existsByUser(user)) {
            throw new BusinessException(
                    "Customer already exists for this user. A user can only have one customer profile.");
        }

        Customer customer = new Customer(
                request.firstName(),
                request.lastName(),
                request.phone(),
                request.shippingAddress(),
                user
        );

        Customer saved = customerRepository.save(customer);

        return customerMapper.toResponse(saved);
    }

    @Override
    public CustomerResponse findByUserId(Long userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found for user id: " + userId));

        return customerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse findCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + customerId));

        return customerMapper.toResponse(customer);
    }

    @Override
    public List<CustomerResponse> findAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toResponseList(customers);
    }

    @Override
    public CustomerResponse updateCustomer(Long userId, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found for user id: " + userId));

        customer.updateDetails(request.firstName(), request.lastName(), request.phone(), request.shippingAddress());
        Customer updated = customerRepository.save(customer);
        return customerMapper.toResponse(updated);

    }
}