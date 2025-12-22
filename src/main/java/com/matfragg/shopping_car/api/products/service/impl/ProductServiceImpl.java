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
import com.matfragg.shopping_car.api.shared.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
    public ProductResponse createProduct(Long userId, CreateProductRequest request) {
        Long sellerId = customerService.findByUserId(userId).id();
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
    public ProductResponse updateProduct(Long sellerId, Long productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId));

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new BusinessException(
                    "You don't have permission to update this product. Only the owner can modify it.");
        }

        if (request.price() != null && request.price() <= 0.0) {
            throw new BusinessException("Price must be greater than zero");
        }

        if (request.stock() != null && request.stock() < 0) {
            throw new BusinessException("Stock cannot be negative");
        }

        product.updateProduct(request.name(), request.description(), request.price(), request.stock(), request.category(), request.imageUrl(), request.active(), product.getAvailable());

        Product updated = productRepository.save(product);

        return productMapper.toResponse(updated);
    }

    @Override
    public void deleteProduct(Long sellerId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId));

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new BusinessException(
                    "You don't have permission to delete this product. Only the owner can delete it.");
        }

        product.setActive(false);
        product.setAvailable(false);
        productRepository.save(product);
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId));

        if (!product.getActive()) {
            throw new ResourceNotFoundException("Product is not available");
        }

        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAllAvailableProducts() {
        List<Product> products = productRepository.findAvailableProducts();
        return productMapper.toResponseList(products);
    }

    @Override
    public List<ProductResponse> getAvailableProductsExcludingSeller(Long userId) {
        Long excludeSellerId = customerService.findByUserId(userId).id();
        List<Product> products = productRepository
                .findAvailableProductsExcludingSeller(excludeSellerId);
        return productMapper.toResponseList(products);
    }

    @Override
    public List<ProductResponse> getMyProducts(Long userId) {
        Long sellerId = customerService.findByUserId(userId).id();

        List<Product> products = productRepository.findProductsBySellerId(sellerId);
        return productMapper.toResponseList(products);
    }

    @Override
    public List<ProductResponse> getProductsBySellerId(Long sellerId) {
        List<Product> products = productRepository.findProductsBySellerId(sellerId);
        return productMapper.toResponseList(products);
    }

    @Override
    public List<ProductResponse> searchProducts(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new BusinessException("Search term cannot be empty");
        }

        List<Product> products = productRepository.findByName(searchTerm.trim());
        return productMapper.toResponseList(products);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String category, Long excludeSellerId) {
        if (category == null || category.trim().isEmpty()) {
            throw new BusinessException("Category cannot be empty");
        }

        List<Product> products;
        if (excludeSellerId != null) {
            products = productRepository.findByCategoryAndSellerIdNot(category, excludeSellerId);
        } else {
            products = productRepository.findByCategory(category);
        }

        return productMapper.toResponseList(products);
    }

    @Override
    public void decreaseStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId));

        if (product.getStock() < quantity) {
            throw new BusinessException(
                    "Insufficient stock for product: " + product.getName() +
                            ". Available: " + product.getStock() + ", Requested: " + quantity);
        }

        product.setStock(product.getStock() - quantity);
        product.setAvailable(product.getStock() > 0);
        productRepository.save(product);
    }

    @Override
    public void increaseStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId));

        product.setStock(product.getStock() + quantity);
        product.setAvailable(true);
        productRepository.save(product);
    }
}
