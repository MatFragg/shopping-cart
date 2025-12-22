package com.matfragg.shopping_car.api.products.controller;

import com.matfragg.shopping_car.api.products.dto.request.CreateProductRequest;
import com.matfragg.shopping_car.api.products.dto.request.UpdateProductRequest;
import com.matfragg.shopping_car.api.products.dto.response.ProductResponse;
import com.matfragg.shopping_car.api.products.service.ProductService;
import com.matfragg.shopping_car.api.shared.dto.ApiResponse;
import com.matfragg.shopping_car.api.shared.security.annotation.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    /**
     * Find all available products, optionally excluding those from a specific seller
     * @param userId ID of the current user to exclude their products
     * @return ApiResponse with list of ProductResponse
     */
    @GetMapping
    @Operation(summary = "Find All Products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findAllProducts(
            @Parameter(description = "Exclude products from this seller (usually current user)")
            @CurrentUserId Long userId) {
        List<ProductResponse> products;
        if (userId != null) {
            products = productService.getAvailableProductsExcludingSeller(userId);
        } else {
            products = productService.getAllAvailableProducts();
        }

        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Create a new product
     * @param userId ID of the authenticated seller
     * @param request Details of the product to create
     * @return ApiResponse with ProductResponse
     */
    @PostMapping
    @Operation(summary = "Create a new Product")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Parameter(hidden = true) @CurrentUserId Long userId,
            @Valid @RequestBody CreateProductRequest request) {

        ProductResponse productResponse = productService.createProduct(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully.", productResponse));
    }

    /**
     * Get products created by the authenticated seller
     * @param userId ID of the authenticated seller
     * @return ApiResponse with list of ProductResponse
     */
    @GetMapping("/my-products")
    @Operation(summary = "Get my products", description = "Returns all products created by the authenticated seller")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findMyProducts(@Parameter(hidden = true) @CurrentUserId Long userId) {

        List<ProductResponse> products = productService.getMyProducts(userId);

        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Search products by name
     * @param query Search term
     * @return ApiResponse with list of ProductResponse
     */
    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search products by name")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(
            @Parameter(description = "Search term")
            @RequestParam String query) {

        List<ProductResponse> products = productService.searchProducts(query);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Get products by category, optionally excluding those from a specific seller
     * @param category Category to filter by
     * @param excludeSellerId Optional seller ID to exclude their products
     * @return ApiResponse with list of ProductResponse
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Returns products filtered by category")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(required = false) Long excludeSellerId) {

        List<ProductResponse> products = productService.getProductsByCategory(category, excludeSellerId);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Get a product by its ID
     * @param productId ID of the product
     * @return ApiResponse with ProductResponse
     */
    @GetMapping("/{productId}")
    @Operation(summary = "Get Product by ID")
    public ResponseEntity<ApiResponse<ProductResponse>> findProductById(@PathVariable Long productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully.", productResponse));
    }

    /**
     * Update an existing product
     * @param userId ID of the authenticated seller
     * @param productId ID of the product to update
     * @param request Details of the product update
     * @return ApiResponse with ProductResponse
     */
    @PutMapping("/{productId}")
    @Operation(summary = "Update Product")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @Parameter(hidden = true) @CurrentUserId Long userId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request) {

        ProductResponse product = productService.updateProduct(userId, productId, request);

        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", product));
    }

    /**
     * Delete a product
     * @param userId ID of the authenticated seller
     * @param productId ID of the product to delete
     * @return ApiResponse with no content
     */
    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete Product")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(hidden = true) @CurrentUserId Long userId,
            @PathVariable Long productId) {

        productService.deleteProduct(userId, productId);

        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }

    /**
     * Get products by a specific seller ID
     * @param sellerId ID of the seller
     * @return ApiResponse with list of ProductResponse
     */
    @GetMapping("/seller/{sellerId}")
    @Operation(summary = "Get Products by Seller ID")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findProductsBySellerId(@PathVariable Long sellerId) {
        List<ProductResponse> productResponse = productService.getProductsBySellerId(sellerId);
        return ResponseEntity.ok(ApiResponse.success("Seller products retrieved.", productResponse));
    }

}