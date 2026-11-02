package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.exception.CategoryNotFoundException;
import com.ecommerce.product.exception.ProductNotFoundException;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        product.setCategory(getCategoryByIdOrThrow(request.categoryId()));
        return productMapper.toResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toResponse).toList();
    }

    public ProductResponse getProductById(Long id) {
        return productMapper.toResponse(getProductByIdOrThrow(id));
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public ProductResponse updateProduct(Long id, ProductRequest updatedProduct) {
        Product product = getProductByIdOrThrow(id);
        product.setCategory(getCategoryByIdOrThrow(updatedProduct.categoryId()));
        productMapper.updateEntity(updatedProduct, product);
        return productMapper.toResponse(productRepository.save(product));
    }

    private Product getProductByIdOrThrow(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    private Category getCategoryByIdOrThrow(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }
}
