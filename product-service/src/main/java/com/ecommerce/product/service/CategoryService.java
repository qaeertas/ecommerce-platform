package com.ecommerce.product.service;

import com.ecommerce.product.dto.CategoryRequest;
import com.ecommerce.product.dto.CategoryResponse;
import com.ecommerce.product.mapper.CategoryMapper;
import com.ecommerce.product.model.Category;
import com.ecommerce.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CategoryRequest category) {
        Category savedCategory = categoryRepository.save(categoryMapper.toEntity(category));
        return categoryMapper.toResponse(savedCategory);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toResponse).toList();
    }
}