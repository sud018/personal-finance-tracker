package com.financetracker.backend.security;
import com.financetracker.backend.config.JwtProperties;
import com.financetracker.backend.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    private JwtService jwtService;
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setSecret("73756468656572736563726574666F726A7774746F6B656E73666F72746869736170706C69636174696F6E");
        jwtProperties.setExpiration(86400000L);
        jwtService = new JwtService(jwtProperties);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        UserDetails userDetails = new User()
                .builder()
                .email("test@test.com")
                .password("password")
                .username("test")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_ShouldReturnCorrectEmail() {
        UserDetails userDetails = new User()
                .builder()
                .email("test@test.com")
                .password("password")
                .username("test")
                .build();

        String token = jwtService.generateToken(userDetails);
        String extractedEmail = jwtService.extractUserName(token);

        assertEquals("test@test.com", extractedEmail);
    }

    @Test
    void isTokenValid_ShouldReturnTrue_ForValidToken() {
        UserDetails userDetails = new User()
                .builder()
                .email("test@test.com")
                .password("password")
                .username("test")
                .build();

        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_ShouldReturnFalse_ForDifferentUser() {
        UserDetails user1 = new User().builder()
                .email("user1@test.com").password("pass").username("user1").build();
        UserDetails user2 = new User().builder()
                .email("user2@test.com").password("pass").username("user2").build();

        String token = jwtService.generateToken(user1);
        boolean isValid = jwtService.isTokenValid(token, user2);

        assertFalse(isValid);
    }
}
