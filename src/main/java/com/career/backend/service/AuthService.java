package com.career.backend.service;

import com.career.backend.dto.AuthDto;
import com.career.backend.dto.UserDto;
import com.career.backend.model.User;
import com.career.backend.repository.UserRepository;
import com.career.backend.security.JwtUtil;
import com.career.backend.security.CustomUserDetails;
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
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                // Allow "ROLE_STUDENT" or "ROLE_USER", completely ignore "ROLE_ADMIN" from frontend
                .role("ROLE_STUDENT") 
                .build();

        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ROLE_USER")) {
            user.setRole("ROLE_USER");
        }

        userRepository.save(user);

        var userDetails = new CustomUserDetails(user);
        String jwtToken = jwtUtil.generateToken(userDetails);

        AuthDto.AuthResponse response = new AuthDto.AuthResponse();
        response.setToken(jwtToken);
        response.setUser(mapToDto(user));
        response.setMessage("User successfully registered");
        return response;
    }

    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var userDetails = new CustomUserDetails(user);
        String jwtToken = jwtUtil.generateToken(userDetails);

        AuthDto.AuthResponse response = new AuthDto.AuthResponse();
        response.setToken(jwtToken);
        response.setUser(mapToDto(user));
        response.setMessage("Login successful");
        return response;
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setPersonalInfo(user.getPersonalInfo());
        return dto;
    }
}
