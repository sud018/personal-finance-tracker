package com.financetracker.backend.service;

import com.financetracker.backend.dto.request.LoginRequest;
import com.financetracker.backend.dto.request.RegisterRequest;
import com.financetracker.backend.dto.response.AuthResponse;
import com.financetracker.backend.entity.User;
import com.financetracker.backend.exception.BadRequestException;
import com.financetracker.backend.exception.DuplicateEmailException;
import com.financetracker.backend.exception.ResourceNotFoundException;
import com.financetracker.backend.repository.CategoryRepository;
import com.financetracker.backend.repository.UserRepository;
import com.financetracker.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CategoryService categoryService;


    public AuthResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateEmailException("Email already exists");
        }
        if(userRepository.existsByUsername(request.getUsername())){
            throw new BadRequestException(("username already exists"));
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullname(request.getFullname())
                .build();

        userRepository.save(user);
        categoryService.seedDefaultCategories(user);
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .username(user.getUsername())
                .fullname(user.getFullname())
                .build();
    }
    public AuthResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .username(user.getUsername())
                .fullname(user.getFullname())
                .build();
    }
}
