package com.financetracker.backend.controller;

import com.financetracker.backend.dto.request.TransactionRequest;
import com.financetracker.backend.dto.response.MonthlySummaryResponse;
import com.financetracker.backend.dto.response.TransactionResponse;
import com.financetracker.backend.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request){
        return ResponseEntity.ok(transactionService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(transactionService.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(@PathVariable Long id,
                                                      @Valid @RequestBody TransactionRequest request){
        return ResponseEntity.ok(transactionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.delete(id));
    }
    @GetMapping("/filter/date-range")
    public ResponseEntity<List<TransactionResponse>> getByDateRange(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(transactionService.getByDateRange(start, end));
    }

    @GetMapping("/filter/category/{categoryId}")
    public ResponseEntity<List<TransactionResponse>> getByCategory(
            @PathVariable Long categoryId) {
        return ResponseEntity.ok(transactionService.getByCategory(categoryId));
    }
    @GetMapping("/summary/monthly")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(transactionService.getMonthlySummary(year, month));
    }

}
