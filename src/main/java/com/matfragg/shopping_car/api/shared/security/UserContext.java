package com.matfragg.shopping_car.api.shared.security;

import com.matfragg.shopping_car.api.customers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserContext {
    private final SecurityUtils securityUtils;
    private final CustomerService customerService;

    public Long getCurrentUserId() {
        return securityUtils.getCurrentUserId();
    }

    public Long getCurrentCustomerId() {
        Long userId = securityUtils.getCurrentUserId();
        return customerService.findByUserId(userId).id();
    }
}