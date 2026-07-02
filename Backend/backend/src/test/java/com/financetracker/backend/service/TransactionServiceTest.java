package com.financetracker.backend.service;

import com.financetracker.backend.dto.request.TransactionRequest;
import com.financetracker.backend.dto.response.TransactionResponse;
import com.financetracker.backend.entity.Category;
import com.financetracker.backend.entity.Transaction;
import com.financetracker.backend.entity.User;
import com.financetracker.backend.exception.ResourceNotFoundException;
import com.financetracker.backend.mapper.TransactionMapper;
import com.financetracker.backend.repository.CategoryRepository;
import com.financetracker.backend.repository.TransactionRespository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRespository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

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

    @Test
    void getById_ShouldThrow_WhenNotFound() {
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getById(999L));
    }

    @Test
    void create_ShouldSaveTransaction() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(BigDecimal.valueOf(100));
        request.setDate(LocalDate.now());
        request.setType("EXPENSE");
        request.setCategoryId(1L);
        request.setDescription("Test");

        Category category = Category.builder().id(1L).name("Food").build();
        Transaction saved = Transaction.builder()
                .amount(BigDecimal.valueOf(100))
                .type("EXPENSE")
                .category(category)
                .build();
        TransactionResponse response = new TransactionResponse();
        response.setAmount(BigDecimal.valueOf(100));

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.save(any())).thenReturn(saved);
        when(transactionMapper.toResponse(any())).thenReturn(response);

        TransactionResponse result = transactionService.create(request);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100), result.getAmount());
        verify(transactionRepository, times(1)).save(any());
    }
}