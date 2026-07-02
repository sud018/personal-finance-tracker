package com.financetracker.backend.service;

import com.financetracker.backend.entity.Transaction;
import com.financetracker.backend.entity.User;
import com.financetracker.backend.repository.TransactionRespository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvExportService {

    private final TransactionRespository transactionRespository;

    private User getCurrentUser(){
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }
    public byte[] exportTransactionsToCsv() throws IOException{
        List<Transaction> transactions = transactionRespository.findByUser(getCurrentUser());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("ID", "Amount", "Type", "Date", "Category", "Description"));

        for(Transaction t: transactions){
            csvPrinter.printRecord(
                    t.getId(),
                    t.getAmount(),
                    t.getType(),
                    t.getDate(),
                    t.getCategory() !=null ? t.getCategory().getName() : "",
                    t.getDescription() !=null ? t.getDescription() : ""
            );
        }
        csvPrinter.flush();
        csvPrinter.close();

        return out.toByteArray();
    }
}
