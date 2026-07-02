package com.financetracker.backend.dto.request;

import com.financetracker.backend.entity.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetRequest {

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "month is required")
    private Integer month;

    @NotNull(message = "year is required")
    private Integer year;

    @NotNull(message = "category is required")
    private Long categoryId;
}
