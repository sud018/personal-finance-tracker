package com.financetracker.backend.mapper;

import com.financetracker.backend.dto.response.CategoryResponse;
import com.financetracker.backend.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
    List<CategoryResponse> toResponseList(List<Category> category);
}
