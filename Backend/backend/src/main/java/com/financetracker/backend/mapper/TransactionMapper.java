package com.financetracker.backend.mapper;

import com.financetracker.backend.dto.response.TransactionResponse;
import com.financetracker.backend.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source="category.id", target="categoryId")
    @Mapping(source="category.name", target="categoryName")
    TransactionResponse toResponse(Transaction transaction);
    List<TransactionResponse> toResponseList(List<Transaction> transaction);
}
