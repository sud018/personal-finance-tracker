package com.financetracker.backend.controller;

import com.financetracker.backend.dto.response.MonthlySummaryResponse;
import com.financetracker.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    @GetMapping("/monthly-summary")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(@RequestParam int month, @RequestParam int year){
         return ResponseEntity.ok(reportService.getMonthlySummary(month, year));
    }

    @GetMapping("/category-breakdown")
    public ResponseEntity<List<Map<String,Object>>> getCategoryBreakdown(@RequestParam int month, @RequestParam int year){
        return ResponseEntity.ok(reportService.getCategoryBreakdown(month, year));
    }

    @GetMapping("/trend")
    public ResponseEntity<List<Map<String, Object>>> getLast6MonthsTrend(){
        return ResponseEntity.ok(reportService.getLast6MonthsTrend());
    }
}
