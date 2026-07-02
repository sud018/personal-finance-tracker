package com.financetracker.backend.security;
import com.financetracker.backend.dto.request.LoginRequest;
import com.financetracker.backend.dto.request.RegisterRequest;
import com.financetracker.backend.dto.response.AuthResponse;
import com.financetracker.backend.entity.User;
import com.financetracker.backend.exception.DuplicateEmailException;
import com.financetracker.backend.repository.UserRepository;
import com.financetracker.backend.security.JwtService;
import com.financetracker.backend.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.financetracker.backend.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_ShouldSaveUserAndReturnToken() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("sudheer");
        request.setEmail("sudheer@test.com");
        request.setPassword("password123");
        request.setFullname("Sudheer");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(jwtService.generateToken(any())).thenReturn("mockToken");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mockToken", response.getToken());
        assertEquals("sudheer@test.com", response.getEmail());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void register_ShouldThrow_WhenEmailExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@test.com");
        request.setUsername("sudheer");

        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsValid() {
        LoginRequest request = new LoginRequest();
        request.setEmail("sudheer@test.com");
        request.setPassword("password123");

        User user = User.builder()
                .email("sudheer@test.com")
                .password("hashedPassword")
                .username("sudheer")
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail("sudheer@test.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any())).thenReturn("mockToken");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mockToken", response.getToken());
    }
}
