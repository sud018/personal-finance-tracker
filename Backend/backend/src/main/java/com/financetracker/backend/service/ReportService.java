package com.financetracker.backend.service;

import com.financetracker.backend.dto.response.MonthlySummaryResponse;
import com.financetracker.backend.entity.Category;
import com.financetracker.backend.entity.Transaction;
import com.financetracker.backend.entity.User;
import com.financetracker.backend.repository.CategoryRepository;
import com.financetracker.backend.repository.TransactionRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRespository transactionRespository;
    private final CategoryRepository categoryRepository;

    private User getCurrentUser(){
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

   public MonthlySummaryResponse getMonthlySummary(int month, int year){
       List<Object[]> results = transactionRespository.getMonthlySummary(getCurrentUser(),year,month);
       BigDecimal totalIncome = BigDecimal.ZERO;
       BigDecimal totalExpense = BigDecimal.ZERO;

       for(Object[] row : results){
         String type = (String) row[0];
         BigDecimal amount = (BigDecimal) row[1];
         if("INCOME".equals(type)) totalIncome=amount;
         else totalExpense=amount;
       }
       return MonthlySummaryResponse.builder()
               .totalIncome(totalIncome)
               .totalExpense(totalExpense)
               .balance(totalIncome.subtract(totalExpense))
               .build();
   }

   public List<Map<String,Object>> getCategoryBreakdown(int month, int year){
        List<Category> categories = categoryRepository.findByUser(getCurrentUser());
        List<Map<String, Object>> breakdown = new ArrayList<>();
        for(Category category: categories){
            List<Transaction> transactions = transactionRespository.findByUserAndMonthAndYear(getCurrentUser(), month, year)
                    .stream().filter(t -> t.getCategory().getId().equals(category.getId()))
                    .collect(Collectors.toList());
            BigDecimal total = transactions.stream()
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if(total.compareTo(BigDecimal.ZERO)>0){
                Map<String, Object> item = new HashMap<>();
                item.put("CategoryID", category.getId());
                item.put("CategoryName", category.getName());
                item.put("type", category.getType());
                item.put("total", total);
                breakdown.add(item);
            }
        }
        return breakdown;
   }

   public List<Map<String, Object>> getLast6MonthsTrend(){
        List<Map<String, Object>> trend = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for(int i=5;i>=0;i--){
            LocalDate date = now.minusMonths(i);
            int month = date.getMonthValue();
            int year = date.getYear();
            List<Object[]> results = transactionRespository.getMonthlySummary(getCurrentUser(), month, year);
            BigDecimal totalIncome = BigDecimal.ZERO;
            BigDecimal totalExpense = BigDecimal.ZERO;
            for(Object[] row: results){
                String type = (String) row[0];
                BigDecimal amount = (BigDecimal) row[1];
                if("INCOME".equals(type)) totalIncome = amount;
                else totalExpense = amount;
            }
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", month);
            monthData.put("year", year);
            monthData.put("totalIncome", totalIncome);
            monthData.put("totalExpense", totalExpense);
            monthData.put("balance", totalIncome.subtract(totalExpense));
            trend.add(monthData);
        }
     return trend;
   }

}
