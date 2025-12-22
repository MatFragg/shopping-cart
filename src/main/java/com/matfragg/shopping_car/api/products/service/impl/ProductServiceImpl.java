package com.matfragg.shopping_car.api.products.service.impl;

import com.matfragg.shopping_car.api.customers.model.entities.Customer;
import com.matfragg.shopping_car.api.customers.repository.CustomerRepository;
import com.matfragg.shopping_car.api.customers.service.CustomerService;
import com.matfragg.shopping_car.api.products.dto.request.CreateProductRequest;
import com.matfragg.shopping_car.api.products.dto.request.UpdateProductRequest;
import com.matfragg.shopping_car.api.products.dto.response.ProductResponse;
import com.matfragg.shopping_car.api.products.mapper.ProductMapper;
import com.matfragg.shopping_car.api.products.model.entities.Product;
import com.matfragg.shopping_car.api.products.repository.ProductRepository;
import com.matfragg.shopping_car.api.products.service.ProductService;
import com.matfragg.shopping_car.api.shared.exceptions.BusinessException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final ProductMapper productMapper;

    public ProductServiceImpl(
            ProductRepository productRepository,
            CustomerRepository customerRepository,
            CustomerService customerService,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse createProduct(Long sellerId, CreateProductRequest request) {
        Customer seller = customerRepository.findById(sellerId).orElseThrow(() -> new BusinessException("Seller not found"));;

        if (seller == null)
            throw new BusinessException("Seller not found with id: " + sellerId);

        if (request.price() == null || request.price() <= 0.0)
            throw new BusinessException("Price must be greater than zero");

        if (request.stock() == null || request.stock() < 0)
            throw new BusinessException("Stock cannot be negative");

        Product product = new Product(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.category(),
                request.imageUrl(),
                seller,
                true,
                request.stock() > 0
        );

        Product saved = productRepository.save(product);

        return productMapper.toResponse(saved);
    }

    @Override
    public ProductResponse updateProduct(Long productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Product not found with id: " + productId));

        product.updateProduct(request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.category(),
                request.imageUrl(),
                request.active(),
                request.stock() > 0
        );
        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }

    @Override
    public ProductResponse deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Product not found with id: " + productId));
        productRepository.delete(product);
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Product not found with id: " + productId));
        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toResponseList(products);
    }

    @Override
    public List<ProductResponse> getProductsBySellerId(Long sellerId) {
        List<Product> products = productRepository.findProductsBySellerId(sellerId);
        return productMapper.toResponseList(products);
    }

    @Override
    public List<ProductResponse> getAvailableProducts(Long excludeSellerId) {
        List<Product> products = productRepository.findByActiveAndAvailableAndSellerIdNot(true, true, excludeSellerId);
        return productMapper.toResponseList(products);
    }
}
