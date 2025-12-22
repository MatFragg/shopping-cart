package com.matfragg.shopping_car.api.customers.controller;

import com.matfragg.shopping_car.api.customers.dto.request.CreateCustomerRequest;
import com.matfragg.shopping_car.api.customers.dto.request.UpdateCustomerRequest;
import com.matfragg.shopping_car.api.customers.dto.response.CustomerResponse;
import com.matfragg.shopping_car.api.customers.service.CustomerService;
import com.matfragg.shopping_car.api.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customers", description = "Endpoints for managing customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Create a new Customer
     * @param request CreateCustomerRequest
     * @return ApiResponse with CustomerResponse
     */
    @PostMapping("/create")
    @Operation(summary = "Create a new Customer", description = "Creates a new customer record.")
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        CustomerResponse customerResponse = customerService.createCustomer(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer created successfully.", customerResponse));
    }

    /**
     * Get Customer by User ID
     * @param userId Long
     * @param request UpdateCustomerRequest
     * @return ApiResponse with CustomerResponse
     */
    @PutMapping("/{userId}/update")
    @Operation(summary = "Update Customer", description = "Updates an existing customer record.")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(@PathVariable Long userId, @Valid @RequestBody UpdateCustomerRequest request) {
        CustomerResponse customerResponse = customerService.updateCustomer(userId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Customer updated successfully.", customerResponse));
    }

    /**
     * Get Customer by User ID
     * @param userId Long
     * @return ApiResponse with CustomerResponse
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Get Customer by User ID", description = "Retrieves a customer record by associated user ID.")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerByUserId(@PathVariable Long userId) {
        CustomerResponse customerResponse = customerService.findByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Customer retrieved successfully.", customerResponse));
    }
}
