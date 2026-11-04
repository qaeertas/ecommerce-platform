package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest product) {
        return productService.createProduct(product);
    }
    
    @GetMapping
    public List<ProductResponse> getAllProducts(@RequestParam(required = false) Long categoryId) {
        if (categoryId != null) {
            return productService.getProductsByCategoryId(categoryId);
        }
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable  Long id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest product) {
        return productService.updateProduct(id, product);
    }

    @PutMapping("/{id}/reduce-stock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reduceStock(
            @PathVariable Long id,
            @RequestParam @Positive(message = "Quantity must be positive") Integer quantity) {
        productService.reduceStock(id, quantity);
    }
}
