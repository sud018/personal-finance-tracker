package com.financetracker.backend.service;

import com.financetracker.backend.dto.request.BudgetRequest;
import com.financetracker.backend.dto.response.BudgetResponse;
import com.financetracker.backend.entity.Budget;
import com.financetracker.backend.entity.Category;
import com.financetracker.backend.entity.User;
import com.financetracker.backend.exception.BadRequestException;
import com.financetracker.backend.exception.ResourceNotFoundException;
import com.financetracker.backend.mapper.BudgetMapper;
import com.financetracker.backend.repository.BudgetRepository;
import com.financetracker.backend.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetMapper budgetMapper;

    private User getCurrentUser(){
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    @Transactional
    public BudgetResponse create(BudgetRequest request){
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        budgetRepository.findByUserAndCategoryAndMonthAndYear(getCurrentUser(), category, request.getMonth(), request.getYear())
                .ifPresent(b -> {throw new BadRequestException("Budget already exists for this category and month");});
        Budget budget = Budget.builder()
                .amount(request.getAmount())
                .month(request.getMonth())
                .year(request.getYear())
                .user(getCurrentUser())
                .category(category)
                .build();
        return budgetMapper.toResponse(budgetRepository.save(budget));
    }

    public List<BudgetResponse> getAllForMonth(int month, int year){
        return budgetMapper.toResponseList(budgetRepository.findByUserAndMonthAndYear(getCurrentUser(),month,year));
    }

    @Transactional
    public BudgetResponse update(Long id, BudgetRequest request) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        if(!budget.getUser().getId().equals(getCurrentUser().getId())){
            throw new AccessDeniedException("unauthorized");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        budget.setAmount(request.getAmount());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());
        budget.setCategory(category);
        return budgetMapper.toResponse(budgetRepository.save(budget));
    }

}
