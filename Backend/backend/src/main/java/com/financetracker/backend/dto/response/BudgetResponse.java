package com.financetracker.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetResponse {
    private Long id;
    private BigDecimal amount;
    private Integer month;
    private Integer year;
    private Long categoryId;
    private String categoryName;
}
