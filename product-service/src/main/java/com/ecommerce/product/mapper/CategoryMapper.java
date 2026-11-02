package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.CategoryRequest;
import com.ecommerce.product.dto.CategoryResponse;
import com.ecommerce.product.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category category);
}
