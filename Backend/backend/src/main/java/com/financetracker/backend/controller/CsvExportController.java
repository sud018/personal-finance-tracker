package com.financetracker.backend.controller;

import com.financetracker.backend.service.CsvExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/export")
public class CsvExportController {
    private final CsvExportService csvExportService;

    @GetMapping("/transactions/csv")
    public ResponseEntity<byte[]> exportTransactionsToCsv() throws IOException{
        byte[] csvData = csvExportService.exportTransactionsToCsv();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "transactions.csv");
        headers.setContentLength(csvData.length);

        return ResponseEntity.ok().headers(headers).body(csvData);
    }
}
