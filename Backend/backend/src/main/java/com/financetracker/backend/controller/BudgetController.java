package com.financetracker.backend.controller;

import com.financetracker.backend.dto.request.BudgetRequest;
import com.financetracker.backend.dto.response.BudgetResponse;
import com.financetracker.backend.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponse> create(@Valid @RequestBody BudgetRequest request){
        return ResponseEntity.ok(budgetService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllForMonth( @RequestParam int month,
                                                                @RequestParam int year){
        return ResponseEntity.ok(budgetService.getAllForMonth(month,year));

    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(@PathVariable Long id, @Valid @RequestBody BudgetRequest request){
        return ResponseEntity.ok(budgetService.update(id,request));
    }

}
