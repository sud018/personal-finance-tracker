package com.financetracker.backend.repository;

import com.financetracker.backend.entity.Budget;
import com.financetracker.backend.entity.Category;
import com.financetracker.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserAndMonthAndYear(User user, Integer month, Integer Year);
    Optional<Budget> findByUserAndCategoryAndMonthAndYear(User user, Category category, Integer month, Integer year);
}
