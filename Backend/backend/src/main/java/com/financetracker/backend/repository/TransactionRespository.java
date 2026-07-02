package com.financetracker.backend.repository;

import com.financetracker.backend.entity.Category;
import com.financetracker.backend.entity.Transaction;
import com.financetracker.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRespository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    Page<Transaction> findByUser(User user, Pageable pageable);
    List<Transaction> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);
    List<Transaction> findByUserAndCategory(User user, Category category);


    @Query("SELECT t from Transaction t where t.user = :user AND YEAR(t.date)=:year AND MONTH(t.date)=:month")
    List<Transaction> findByUserAndMonthAndYear(@Param("user") User user, @Param("year") int year, @Param("month") int month);

    @Query("SELECT t.type, SUM(t.amount) from Transaction t where t.user=:user AND YEAR(t.date)=:year AND MONTH(t.date)=:month GROUP by t.type")
    List<Object[]> getMonthlySummary(@Param("user") User user, @Param("year") int year, @Param("month") int month);
}
