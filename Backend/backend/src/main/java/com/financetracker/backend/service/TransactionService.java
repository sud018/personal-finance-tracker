package com.financetracker.backend.service;

import com.financetracker.backend.dto.request.TransactionRequest;
import com.financetracker.backend.dto.response.MonthlySummaryResponse;
import com.financetracker.backend.dto.response.TransactionResponse;
import com.financetracker.backend.entity.Category;
import com.financetracker.backend.entity.Transaction;
import com.financetracker.backend.entity.User;
import com.financetracker.backend.exception.ResourceNotFoundException;
import com.financetracker.backend.mapper.TransactionMapper;
import com.financetracker.backend.repository.CategoryRepository;
import com.financetracker.backend.repository.TransactionRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRespository transactionRespository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;

    private User getCurrentUser(){
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    public TransactionResponse create(TransactionRequest request){
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .date(request.getDate())
                .type(request.getType())
                .user(getCurrentUser())
                .category(category)
                .build();

        return transactionMapper.toResponse(transactionRespository.save(transaction));
    }
    public Page<TransactionResponse> getAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return transactionRespository.findByUser(getCurrentUser(), pageable).map(transaction -> transactionMapper.toResponse(transaction));
    }

    public TransactionResponse getById(Long id){
        Transaction transaction = transactionRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if(!transaction.getUser().getId().equals(getCurrentUser().getId())){
            throw new AccessDeniedException(("Unauthorized"));
        }
        return transactionMapper.toResponse(transaction);
    }

    public TransactionResponse update(Long id, TransactionRequest request){
        Transaction transaction = transactionRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if(!transaction.getUser().getId().equals(getCurrentUser().getId())){
            throw new AccessDeniedException("Unauthorized");
        }
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());
        transaction.setCategory(category);
        return transactionMapper.toResponse(transactionRespository.save(transaction));
    }

    public String delete(Long id){
        Transaction transaction = transactionRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if(!transaction.getUser().getId().equals(getCurrentUser().getId())){
            throw new AccessDeniedException("Unauthorized");
        }
        transactionRespository.delete(transaction);
        return "Deleted Successfully";
    }

    public List<TransactionResponse> getByDateRange(LocalDate start, LocalDate end){
        return transactionMapper.toResponseList(transactionRespository
                .findByUserAndDateBetween(getCurrentUser(), start, end));
    }

    public List<TransactionResponse> getByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return transactionMapper.toResponseList(
                transactionRespository.findByUserAndCategory(getCurrentUser(), category));
    }

    public MonthlySummaryResponse getMonthlySummary(int year, int month){
        List<Object[]> results = transactionRespository.getMonthlySummary(getCurrentUser(), year, month);

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for(Object[] row: results){
            String type = (String) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            if("INCOME".equals(type)) totalIncome = amount;
            else totalExpense = amount;
        }
       return MonthlySummaryResponse.builder()
               .totalIncome(totalIncome)
               .totalExpense(totalExpense)
               .balance(totalIncome.subtract(totalExpense))
               .build();
    }
}
