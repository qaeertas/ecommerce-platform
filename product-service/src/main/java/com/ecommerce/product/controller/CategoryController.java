package com.ecommerce.product.controller;

import com.ecommerce.product.dto.CategoryRequest;
import com.ecommerce.product.dto.CategoryResponse;
import com.ecommerce.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest category) {
        return categoryService.createCategory(category);
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
