package com.matfragg.shopping_car.api.products.service.impl;

import com.matfragg.shopping_car.api.customers.dto.response.CustomerResponse;
import com.matfragg.shopping_car.api.customers.service.CustomerService;
import com.matfragg.shopping_car.api.products.dto.request.CreateProductRequest;
import com.matfragg.shopping_car.api.products.dto.response.ProductResponse;
import com.matfragg.shopping_car.api.products.mapper.ProductMapper;
import com.matfragg.shopping_car.api.products.model.entities.Product;
import com.matfragg.shopping_car.api.products.repository.ProductRepository;
import com.matfragg.shopping_car.api.products.service.ProductService;
import com.matfragg.shopping_car.api.shared.exceptions.BusinessException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CustomerService customerService;
    private final ProductMapper productMapper;

    public ProductServiceImpl(
            ProductRepository productRepository,
            CustomerService customerService,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.customerService = customerService;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse createProduct(Long sellerId, CreateProductRequest request) {
        CustomerResponse seller = customerService.findById(sellerId);

        if (request.price().compareTo((double) 0) <= 0)
            throw new BusinessException("Price must be greater than zero");

        if (request.stock() < 0)
            throw new BusinessException("Stock cannot be negative");

        Product product = new Product(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.category(),
                request.imageUrl(),
                sellerId,
                true,
                request.stock() > 0
        );

        Product saved = productRepository.save(product);

        return productMapper.toResponse(saved);
    }

    @Override
    public List<ProductResponse> findBySellerId(Long sellerId) {
        List<Product> products = productRepository.findProductsBySellerId(sellerId);
        return productMapper.toResponseList(products);
    }

    @Override
    public List<ProductResponse> findAvailableProducts(Long excludeSellerId) {
        List<Product> products = productRepository.findByActiveAndAvailableAndSellerIdNot(true, true, excludeSellerId);
        return productMapper.toResponseList(products);
    }
}
