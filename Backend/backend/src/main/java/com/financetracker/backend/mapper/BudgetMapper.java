package com.financetracker.backend.mapper;

import com.financetracker.backend.dto.response.BudgetResponse;
import com.financetracker.backend.entity.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BudgetMapper {
    @Mapping(source="category.id", target="categoryId")
    @Mapping(source="category.name", target = "categoryName")
    BudgetResponse toResponse(Budget budget);
    List<BudgetResponse> toResponseList(List<Budget> budget);
}
