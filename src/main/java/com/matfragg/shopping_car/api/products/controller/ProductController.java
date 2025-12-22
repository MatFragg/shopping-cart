package com.matfragg.shopping_car.api.products.controller;

import com.matfragg.shopping_car.api.products.dto.request.CreateProductRequest;
import com.matfragg.shopping_car.api.products.dto.request.UpdateProductRequest;
import com.matfragg.shopping_car.api.products.dto.response.ProductResponse;
import com.matfragg.shopping_car.api.products.service.ProductService;
import com.matfragg.shopping_car.api.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Endpoints for managing products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Find All Products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findAllProducts() {
        List<ProductResponse> productResponse = productService.getAllProducts();
        String message = productResponse.isEmpty() ? "No products found." : "All products retrieved successfully.";
        return ResponseEntity.ok(ApiResponse.success(message, productResponse));
    }

    @PostMapping
    @Operation(summary = "Create a new Product")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @RequestParam Long sellerId,
            @Valid @RequestBody CreateProductRequest request) {

        ProductResponse productResponse = productService.createProduct(sellerId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully.", productResponse));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get Product by ID")
    public ResponseEntity<ApiResponse<ProductResponse>> findProductById(@PathVariable Long productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully.", productResponse));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update Product")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request) {

        ProductResponse productResponse = productService.updateProduct(productId, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully.", productResponse));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete Product")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully.", null));
    }

    @GetMapping("/seller/{sellerId}")
    @Operation(summary = "Get Products by Seller ID")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findProductsBySellerId(@PathVariable Long sellerId) {
        List<ProductResponse> productResponse = productService.getProductsBySellerId(sellerId);
        return ResponseEntity.ok(ApiResponse.success("Seller products retrieved.", productResponse));
    }

    @GetMapping("/available-except/{excludeSellerId}")
    @Operation(summary = "Find Available Products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findAvailableProducts(@PathVariable Long excludeSellerId) {
        List<ProductResponse> productResponse = productService.getAvailableProducts(excludeSellerId);
        return ResponseEntity.ok(ApiResponse.success("Available products retrieved.", productResponse));
    }
}