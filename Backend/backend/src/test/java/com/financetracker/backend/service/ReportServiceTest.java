package com.financetracker.backend.service;
import com.financetracker.backend.dto.response.MonthlySummaryResponse;
import com.financetracker.backend.repository.CategoryRepository;
import com.financetracker.backend.repository.TransactionRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.financetracker.backend.service.ReportService;
import com.financetracker.backend.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private TransactionRespository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void getMonthlySummary_ShouldReturnZeros_WhenNoTransactions() {
        when(transactionRepository.getMonthlySummary(any(), anyInt(), anyInt()))
                .thenReturn(List.of());

        MonthlySummaryResponse response = reportService.getMonthlySummary(6, 2026);

        assertNotNull(response);
        assertEquals(BigDecimal.ZERO, response.getTotalIncome());
        assertEquals(BigDecimal.ZERO, response.getTotalExpense());
        assertEquals(BigDecimal.ZERO, response.getBalance());
    }

    @Test
    void getMonthlySummary_ShouldCalculateCorrectly() {
        Object[] incomeRow = {"INCOME", BigDecimal.valueOf(3000)};
        Object[] expenseRow = {"EXPENSE", BigDecimal.valueOf(1500)};

        when(transactionRepository.getMonthlySummary(any(), anyInt(), anyInt()))
                .thenReturn(List.of(incomeRow, expenseRow));

        MonthlySummaryResponse response = reportService.getMonthlySummary(6, 2026);

        assertEquals(BigDecimal.valueOf(3000), response.getTotalIncome());
        assertEquals(BigDecimal.valueOf(1500), response.getTotalExpense());
        assertEquals(BigDecimal.valueOf(1500), response.getBalance());
    }
    @BeforeEach
    void setUp() {
        User mockUser = User.builder()
                .email("test@test.com")
                .username("test")
                .password("password")
                .build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null, List.of());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
}
